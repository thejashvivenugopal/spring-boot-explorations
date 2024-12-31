package com.redis.redis.service;

import com.redis.redis.model.Customers;
import com.redis.redis.repo.CustomerRepo;
import com.redis.redis.request.CreateCustomerRequest;
import com.redis.redis.request.UpdateCustomerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo repo;
    public ResponseEntity<?> addCustomer(CreateCustomerRequest request) {
        Customers customers = repo.findByMobileNumber(request.getMobileNumber());
        if (customers != null){
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(customers);
        }
        customers = new Customers();
        customers.setFirstName(request.getFirstName());
        customers.setLastName(request.getLastName());
        customers.setMobileNumber(request.getMobileNumber());

        repo.save(customers);
        return ResponseEntity.ok().body(customers);
    }

    public ResponseEntity<?> fetchCustomer(String mobileNumber) {
        if (StringUtils.hasLength(mobileNumber)){
            Customers customers = repo.findByMobileNumber(mobileNumber);
            if (customers == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

            return ResponseEntity.ok().body(customers);
        }

        List<Customers> customers = repo.findAll();

        if (customers.size() == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok().body(customers);
    }

    public ResponseEntity<?> updateCustomer(UpdateCustomerRequest request) {
        Customers customers = repo.findByMobileNumber(request.getMobileNumber());
        if (customers == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        customers.setFirstName(request.getFirstName());
        customers.setLastName(request.getLastName());
        repo.save(customers);
        return ResponseEntity.ok().body(customers);
    }

    public ResponseEntity<?> deleteCustomer(String mobileNumber) {
        Customers customers = repo.findByMobileNumber(mobileNumber);
        if (customers == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        repo.delete(customers);
        return ResponseEntity.status(HttpStatus.GONE).build();
    }
}
