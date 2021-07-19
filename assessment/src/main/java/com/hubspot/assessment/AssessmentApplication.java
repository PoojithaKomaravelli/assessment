package com.hubspot.assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class AssessmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssessmentApplication.class, args);
	}

}
