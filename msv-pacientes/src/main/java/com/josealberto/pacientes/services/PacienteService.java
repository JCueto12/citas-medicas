package com.josealberto.pacientes.services;

import com.josealberto.commons.dto.PacienteRequest;
import com.josealberto.commons.dto.PacienteResponse;
import com.josealberto.commons.services.CrudService;



public interface PacienteService extends CrudService<PacienteRequest, PacienteResponse> {

    PacienteResponse obtenerPorIdTodos(Long id);
}
