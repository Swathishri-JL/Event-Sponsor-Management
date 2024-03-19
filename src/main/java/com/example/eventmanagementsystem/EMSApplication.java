// Do not modify the code in this file
package com.example.eventmanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.eventmanagementsystem.repository")
public class EMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(EMSApplication.class, args);
	}

}
