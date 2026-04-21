package com.josealberto.medicos.controllers;


import com.josealberto.medicos.services.MedicoServiceImpl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josealberto.commons.controllers.CommonController;
import com.josealberto.commons.dto.MedicoResponse;
import com.josealberto.commons.dto.MedicoRequest;
import com.josealberto.medicos.services.MedicoService;

import jakarta.validation.constraints.Positive;


@RestController
public class MedicoController extends CommonController<MedicoRequest, MedicoResponse, MedicoService>{
    
	public MedicoController(MedicoService serice, MedicoServiceImpl medicoServiceImpl) {
		super(serice);
	}


	@GetMapping("/id-medico/{id}")
    public ResponseEntity<MedicoResponse> obtenerPorIdSinEstado(
            @PathVariable  @Positive(message = "El ID debe ser positivo") Long id
    ){
        return ResponseEntity.ok(service.obtenerPorIdTodos(id));
    }
	
	@PutMapping("{idMedico}/disponibilidad/{idDisponibilidad}")
	public ResponseEntity<Void> actualizarDisponibilidadMedico(
		@PathVariable @Positive(message = "El idMedico debe ser positivo") Long idMedico,
		@PathVariable @Positive(message = "El idMedico debe ser positivo") Long idDisponibilidad){
		service.actualizarEstado(idMedico, idDisponibilidad);
		return ResponseEntity.noContent().build();
	}
		}