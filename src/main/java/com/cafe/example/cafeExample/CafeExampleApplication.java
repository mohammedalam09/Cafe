package com.cafe.example.cafeExample;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CafeExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeExampleApplication.class, args);
		System.out.println("Application Started!!!");
	}

	@Bean
	ModelMapper getModelMapper() {
		return new ModelMapper();
	}

}
