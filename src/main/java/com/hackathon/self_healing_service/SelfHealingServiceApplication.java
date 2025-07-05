package com.hackathon.self_healing_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SelfHealingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SelfHealingServiceApplication.class, args);
	}

}
