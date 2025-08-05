package com.vighnaharta.nursery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(scanBasePackages = "com.vighnaharta.nursery")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class VighnahartaNurseryApplication {

	public static void main(String[] args) {
		SpringApplication.run(VighnahartaNurseryApplication.class, args);
	}

}

