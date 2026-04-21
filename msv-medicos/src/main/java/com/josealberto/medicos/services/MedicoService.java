package com.josealberto.medicos.services;

import com.josealberto.commons.dto.MedicoRequest;
import com.josealberto.commons.dto.MedicoResponse;
import com.josealberto.commons.services.CrudService;

public interface MedicoService extends CrudService<MedicoRequest, MedicoResponse> {
	
	MedicoResponse obtenerPorIdTodos(Long id);
	
	void actualizarEstado(Long idMedico, Long idEspecialidad);

}
