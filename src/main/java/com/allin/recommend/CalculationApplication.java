package com.allin.recommend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CalculationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalculationApplication.class, args);
	}
}
