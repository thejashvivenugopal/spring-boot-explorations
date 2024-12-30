package com.ratelimit.rateLimit.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/security/rate-limit")
public class RateLimitController {

    @GetMapping
    public ResponseEntity<?> rateLimitTest1(){
        return ResponseEntity.ok().body("RUNNING SUCCESSFULLY");
    }

    @GetMapping("/two")
    public ResponseEntity<?> rateLimitTest2(){
        return ResponseEntity.ok().body("RUNNING SUCCESSFULLY");
    }

    @GetMapping("/disable")
    public ResponseEntity<?> rateLimitRemovedTest(){
        return ResponseEntity.ok().body("RUNNING SUCCESSFULLY");
    }
}
