package com.josealberto.citas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.josealberto.citas", "com.josealberto.commons;"})
public class MsvCitasApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvCitasApplication.class, args);
	}

}
