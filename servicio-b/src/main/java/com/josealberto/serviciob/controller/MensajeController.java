package com.josealberto.serviciob.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josealberto.serviciob.client.ServicioAClient;

@RestController
public class MensajeController {

	private final ServicioAClient servicioAClient;
	

	public MensajeController(ServicioAClient servicioAClient) {
		this.servicioAClient = servicioAClient;
	}

	@GetMapping
	public ResponseEntity<String> mensaje() {
		return ResponseEntity.ok("Hola Mundo dice service B");
	}

	@GetMapping("/desde-a")
	public ResponseEntity<String> mensajeSaludo() {
		return ResponseEntity.ok("Hola Mundo dice: " + servicioAClient.obtenerSaludo());
	}
}
