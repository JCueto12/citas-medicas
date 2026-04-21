package com.josealberto.servicioa.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "servicio-b")
public interface ServicioBClient {

	@GetMapping
	String obtenerMensaje();
}
