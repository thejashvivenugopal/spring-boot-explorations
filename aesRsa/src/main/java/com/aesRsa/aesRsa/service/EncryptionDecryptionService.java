package com.aesRsa.aesRsa.service;

import com.aesRsa.aesRsa.utils.EncryptDecryptUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

@Service
public class EncryptionDecryptionService {
    public ResponseEntity<?> encryptDecryptAes(String string) {
        return ResponseEntity.ok().body(EncryptDecryptUtils.aes(string));
    }

    public ResponseEntity<?> encryptDecryptRsa(String string) {
        try {
            Map map = new HashMap<>();
            KeyPair keyPair = EncryptDecryptUtils.generateKeyPair();
            String rsaEncrypted = EncryptDecryptUtils.encrypt(string, keyPair.getPublic());
            map.put("rsaEncrypted",rsaEncrypted);
            String rsaDecrypted = EncryptDecryptUtils.decrypt(rsaEncrypted, keyPair.getPrivate());
            map.put("rsaDecrypted",rsaDecrypted);

            return ResponseEntity.ok().body(map);

        } catch (Exception e) {
            System.out.println("e = " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }    }
}
