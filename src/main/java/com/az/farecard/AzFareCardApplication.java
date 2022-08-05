package com.az.farecard;

import com.az.farecard.entity.Role;
import com.az.farecard.entity.User;
import com.az.farecard.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@OpenAPIDefinition(info = @Info(title = "Fare Care Service", version = "1.0",
		description = "It's a simple Fare Care Service where predefined cards can perform Swipe In/Out functionality for user journey"))
@EnableJpaAuditing
@SpringBootApplication
public class AzFareCardApplication {


	public static void main(String[] args) {
		SpringApplication.run(AzFareCardApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(UserService userService) {
		return args -> {
			userService.saveUserRole(new Role(null, "ROLE_USER"));
			userService.saveUser(new User(null, "azhar", passwordEncoder().encode("1234"),
					"Azhar Mobeen", new ArrayList<>()));
			userService.assignUserRole("azhar", "ROLE_USER");
			List<User> users = userService.fetchAllUser();
			users.forEach(user -> log.info("User details ::: {}", user));
		};
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}