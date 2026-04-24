package com.josealberto.citas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.josealberto.citas.dto.CitaRequest;
import com.josealberto.citas.dto.CitaResponse;
import com.josealberto.citas.services.CitaService;
import com.josealberto.commons.controllers.CommonController;

import jakarta.validation.constraints.Positive;

@RestController
@Validated
public class CitaController extends CommonController<CitaRequest, CitaResponse, CitaService> {

	public CitaController(CitaService service) {
		super(service);
		// TODO Auto-generated constructor stub
	}
	
	

	@PatchMapping("/{idCita}/estado/{idEstado}")
	public ResponseEntity<CitaResponse> actualizarEstado(
			@PathVariable @Positive(message = "El idCita debe ser positivo") Long idCita,
			@PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado) {
		return  ResponseEntity.ok(service.actualizarEstadoCita(idCita, idEstado));
	}
	
	@GetMapping("/id-medico/{idMedico}/citas-asignadas")
	public ResponseEntity<Void> medicoTieneCitasAsignadas(
				@PathVariable @Positive(message = "El idMedico debe ser positivo") Long idMedico){
					service.medicoTieneCitasAsignadas(idMedico);
					return ResponseEntity.noContent().build();					
				}
				
	@GetMapping("/id-paciente/{idPaciente}/citas-asignadas")
	public ResponseEntity<Void> pacientemedicoTieneCitasAsignadas(
				@PathVariable @Positive(message = "El idPaciente debe ser positivo") Long idPaciente){
					service.pacienteTieneCitasAsignadas(idPaciente);
					return ResponseEntity.noContent().build();					
				}	
}
