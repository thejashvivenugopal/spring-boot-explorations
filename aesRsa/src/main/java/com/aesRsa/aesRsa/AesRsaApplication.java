package com.aesRsa.aesRsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AesRsaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AesRsaApplication.class, args);
		System.out.println("http://localhost:9000/swagger-ui.html");
	}

}
