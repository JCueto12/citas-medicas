package com.josealberto.pacientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.josealberto.pacientes", "com.josealberto.commons;"})
public class MsvPacientesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvPacientesApplication.class, args);
	}

}
