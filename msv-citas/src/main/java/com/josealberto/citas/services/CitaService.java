package com.josealberto.citas.services;

import com.josealberto.citas.dto.CitaRequest;
import com.josealberto.citas.dto.CitaResponse;
import com.josealberto.commons.services.CrudService;

public interface CitaService extends CrudService<CitaRequest, CitaResponse> {

	public CitaResponse actualizarEstadoCita(Long idCita, Long idEstado);
	
	void medicoTieneCitasAsignadas(Long idMedico);
	
	void pacienteTieneCitasAsignadas(Long idPaciente);
}
