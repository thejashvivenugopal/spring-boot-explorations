package com.redis.redis.controller;

import com.redis.redis.request.CreateCustomerRequest;
import com.redis.redis.request.UpdateCustomerRequest;
import com.redis.redis.service.CustomerService;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomersController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/customer")
    @Timed(value = "requests.count.save")
    public ResponseEntity<?> addCustomer(@RequestBody CreateCustomerRequest request){
        return customerService.addCustomer(request);
    }

    @GetMapping("/customer")
    @Timed(value = "requests.count.getByMobileNumber")
    public ResponseEntity<?> fetchCustomer(@RequestHeader(required = false) String mobileNumber){
        System.out.println("timed");
        return customerService.fetchCustomer(mobileNumber);
    }

    @PutMapping("/customer")
    @Timed(value = "requests.count.update")
    public ResponseEntity<?> updateCustomer(@RequestBody UpdateCustomerRequest request){
        return customerService.updateCustomer(request);
    }

    @DeleteMapping("/customer")
    @Timed(value = "requests.count.delete")
    public ResponseEntity<?> deleteCustomer(@RequestHeader String mobileNumber){
       return customerService.deleteCustomer(mobileNumber);
    }
}
