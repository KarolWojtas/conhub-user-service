package com.karol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConhubUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConhubUserServiceApplication.class, args);
	}
}
