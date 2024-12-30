package com.aesRsa.aesRsa.utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class EncryptDecryptUtils {

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public static String encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(cipherBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * AES
     * */

    private static final String SECRET_KEY = "123456789";
    private static final String SALTVALUE = "abcdefg";

    /* Encryption Method */
    public static byte[] generateRandomIV() {
        // Create a random byte array for the IV
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }


    public static Map aes(String string){
        Map map = new HashMap<>();
        byte[] iv = generateRandomIV();
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        /* Create factory for secret keys. */
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        /* PBEKeySpec class implements KeySpec interface. */
        KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALTVALUE.getBytes(), 65536, 256);
        SecretKey tmp = null;
        try {
            tmp = factory.generateSecret(spec);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        String aesEncryptedString = aesEncrypt(string,secretKey,ivspec);
        map.put("aesEncryptedString",aesEncryptedString);

        String aesDecryptedString = aesDecrypt(aesEncryptedString,secretKey,ivspec);
        map.put("aesDecryptedString",aesDecryptedString);

        return map;
    }

    public static String aesEncrypt(String strToEncrypt, SecretKeySpec secretKey,IvParameterSpec ivspec)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

            /* Retruns encrypted value. */
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        }
        catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException |
               BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e)
        {
            System.out.println("Error occured during encryption: " + e.toString());
        }
        return null;
    }

    /* Decryption Method */
    public static String aesDecrypt(String strToDecrypt,SecretKeySpec secretKey,IvParameterSpec ivspec)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            /* Retruns decrypted value. */
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException |
               BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e)
        {
            System.out.println("Error occured during decryption: " + e.toString());
        }
        return null;
    }
}
