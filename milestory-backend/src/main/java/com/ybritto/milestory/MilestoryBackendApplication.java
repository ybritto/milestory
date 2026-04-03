package com.ybritto.milestory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MilestoryBackendApplication {

	public static void main(String[] args) {
		log.debug("Starting Milestory backend application");
		SpringApplication.run(MilestoryBackendApplication.class, args);
	}

}
