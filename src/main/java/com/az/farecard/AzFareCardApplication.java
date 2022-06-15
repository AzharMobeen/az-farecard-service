package com.az.farecard;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(info = @Info(title = "Fare Care Service", version = "1.0",
		description = "It's a simple Fare Care Service where predefined cards can perform Swipe In/Out functionality for user journey"))
@EnableJpaAuditing
@SpringBootApplication
public class AzFareCardApplication {

	public static void main(String[] args) {
		SpringApplication.run(AzFareCardApplication.class, args);
	}
}