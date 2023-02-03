package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class GiftCertificatesSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(GiftCertificatesSystemApplication.class, args);
	}

}
