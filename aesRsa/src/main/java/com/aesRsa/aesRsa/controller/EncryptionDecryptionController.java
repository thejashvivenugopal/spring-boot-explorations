package com.aesRsa.aesRsa.controller;

import com.aesRsa.aesRsa.service.EncryptionDecryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EncryptionDecryptionController {

    @Autowired
    private EncryptionDecryptionService service;

    @GetMapping("/aes")
    public ResponseEntity<?> encryptDecryptAes(@RequestHeader String string){
        return service.encryptDecryptAes(string);
    }

    @GetMapping("/rsa")
    public ResponseEntity<?> encryptDecryptRsa(@RequestHeader String string){
        return service.encryptDecryptRsa(string);
    }
}
