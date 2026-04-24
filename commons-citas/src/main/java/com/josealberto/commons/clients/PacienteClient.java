package com.josealberto.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.josealberto.commons.dto.PacienteResponse;

@FeignClient(name = "msv-pacientes")
public interface PacienteClient {
	
	@GetMapping("/{id}")
	PacienteResponse obtenerPorId(@PathVariable Long id);
	
	@GetMapping("/id-pacientes/{id}")
	PacienteResponse obtenerPorIdTodos(@PathVariable Long id);
}
