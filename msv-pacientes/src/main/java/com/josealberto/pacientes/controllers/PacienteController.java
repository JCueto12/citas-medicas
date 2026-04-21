package com.josealberto.pacientes.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.josealberto.commons.controllers.CommonController;
import com.josealberto.commons.dto.PacienteRequest;
import com.josealberto.commons.dto.PacienteResponse;
import com.josealberto.pacientes.services.PacienteService;

import jakarta.validation.constraints.Positive;

@RestController
public class PacienteController extends CommonController<PacienteRequest, PacienteResponse, PacienteService> {
	

	public PacienteController(PacienteService service) {
		super(service);
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/id-pacientes/{id}")
    public ResponseEntity<PacienteResponse> obtenerMedicoTodos(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id){
        return ResponseEntity.ok(service.obtenerPorIdTodos(id));
    }
	
}
