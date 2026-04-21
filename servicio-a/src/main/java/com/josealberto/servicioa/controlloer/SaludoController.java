package com.josealberto.servicioa.controlloer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josealberto.servicioa.client.ServicioBClient;

@RestController
public class SaludoController {
	
	private final ServicioBClient servicioBClient;
	
	

	public SaludoController(ServicioBClient servicioBClient) {
		this.servicioBClient = servicioBClient;
	}


	@GetMapping
	public ResponseEntity<String> saludo() {
		return ResponseEntity.ok("Servicio A dice Hola Mundo");
	}
	
	@GetMapping("/desde-b")
	public ResponseEntity<String> saludoDesde() {
		return ResponseEntity.ok("Servicio A dice:  " + servicioBClient.obtenerMensaje());
	}
}
