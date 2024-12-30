package com.ratelimit.rateLimit.config;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

 /** Rate limiting is a strategy to limit access to APIs.
 * It restricts the number of API calls that a client can make within a certain time frame.
 * This helps defend the API against overuse, both unintentional and malicious.
 * */
@Component
public class RateLimitConfig implements Filter {

    private int MAX_REQUEST = 3;

     /**
     * The key-value pairs in the cache represent IP addresses and their corresponding request counts.
     */
    private LoadingCache<String,Integer> requestCountsPerIpAddress;

    private Set<String> urls = new HashSet<>(Arrays.asList(
            "/api/v1/security/rate-limit",
            "/api/v1/security/rate-limit/two"
    ));

    //The doFilter method is part of the Filter interface in the Java Servlet API. It's a crucial method that gets called by the container when a request/response pair is sent to the server. The doFilter method allows you to perform operations on the request or response or to pass the request along the filter chain.
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String some = httpServletRequest.getRequestURI();
        System.out.println("url = " + some);

        String finalUri = (httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length()));

        if(urls.contains(finalUri)) {
            /** clientHashId contains url+/[clientHashId or IP] */
            String clientHashId;
            if (StringUtils.isEmpty(httpServletRequest.getHeader("client-hash-id")))
                clientHashId = finalUri+"/"+getClientIP((HttpServletRequest) servletRequest);
            else clientHashId = finalUri+"/"+httpServletRequest.getHeader("client-hash-id");

            if (isMaxRequestsPerSecond(clientHashId)) {
                httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                httpServletResponse.getWriter().print(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build());
                return;
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    private String getClientIP(HttpServletRequest servletRequest) {


        String ip = servletRequest.getHeader("X-Forwarded-For");

        if(ip == null)
        {
            /** Get the ipAddress of the client */
            ip = servletRequest.getRemoteAddr();
            return ip;
        }
        return ip.split(",")[0];
    }

    private boolean isMaxRequestsPerSecond(String clientHashId) {
        int req =0;
        try {
             /**
              Fetching the number of requests using the key
             */
            req = requestCountsPerIpAddress.get(clientHashId);
            if(req>MAX_REQUEST)
            {
                System.out.println("EXCEEDED = " + new Date());
                requestCountsPerIpAddress.put(clientHashId,req);
                return true;
            }
        }
        catch (Exception e) {
            req =0;
        }
        req++;
        requestCountsPerIpAddress.put(clientHashId,req);
        return false;
    }

     /**
     *this cache is initialized using Guava's CacheBuilder.newBuilder() method,
      and it is configured with an expiration time of 1 minute for each entry
     */
    public RateLimitConfig(){
        super();
        requestCountsPerIpAddress = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build(new CacheLoader<String, Integer>() {
            public Integer load(String s) throws Exception {
                return null;
            }
        });
    }

}
