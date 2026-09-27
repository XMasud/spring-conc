package com.example.spring_conc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SpringConcApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringConcApplication.class, args);
	}

}
