package com.josealberto.medicos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.josealberto.medicos", "com.josealberto.commons;"})
public class MsvMedicosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvMedicosApplication.class, args);
	}

}
