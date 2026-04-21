package com.josealberto.serviciob.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "servicio-a")
public interface ServicioAClient {
	
	@GetMapping
	String obtenerSaludo();

}
