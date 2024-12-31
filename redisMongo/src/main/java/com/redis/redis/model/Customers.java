package com.redis.redis.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
@Data
public class Customers implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String mobileNumber;
    private String firstName;
    private String lastName;
}
