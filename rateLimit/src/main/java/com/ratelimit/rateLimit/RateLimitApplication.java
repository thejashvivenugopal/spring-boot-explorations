package com.ratelimit.rateLimit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RateLimitApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateLimitApplication.class, args);
		System.out.println("http://localhost:9090/swagger-ui/index.html");
}

}
