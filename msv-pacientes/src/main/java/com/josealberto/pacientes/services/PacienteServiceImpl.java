package com.josealberto.pacientes.services;


import com.josealberto.commons.clients.CitaClient;
import com.josealberto.commons.dto.PacienteRequest;
import com.josealberto.commons.dto.PacienteResponse;
import com.josealberto.pacientes.entities.Paciente;
import com.josealberto.commons.enums.EstadoRegistro;
import com.josealberto.commons.exceptions.RecursoNoEncontradoException;
import com.josealberto.pacientes.mappers.PacienteMapper;
import com.josealberto.pacientes.repositories.PacienteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;
    private final CitaClient citaClient;

    @Override
    @Transactional( readOnly = true)
    public List<PacienteResponse> listar() {
        log.info("Listando todos los pacientes activos");
        return pacienteRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(pacienteMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional( readOnly = true)
    public PacienteResponse obtenerPorId(Long id) {
    	log.info("Buscando paciente con estado activo");
        return pacienteMapper.entidadAResponse(obtenerPacienteActivoOException(id));

    }

    @Transactional( readOnly = true)
    public PacienteResponse obtenerPorIdTodos(Long id) {
    	log.info("Buscando paciente sin estado con id {}", id);
    	return pacienteMapper.entidadAResponse(
    			pacienteRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("Paciente no encontrado con id: " + id)));
    }

    @Override
    public PacienteResponse registrar(PacienteRequest request) {
        if (request == null) return null;
        log.info("Registrando nuevo paciente...");

        validarUnicidad(request);

        Paciente paciente = pacienteMapper.requestAEntidad(request);
        
        paciente.setImc(paciente.obtenerImc());
        paciente.setNumExpediente(paciente.generarNumExpediente());
        
        pacienteRepository.save(paciente);
        log.info("Paciente registrado con exito");
        return pacienteMapper.entidadAResponse(paciente);
    }

    @Override
    public PacienteResponse actualizar(PacienteRequest request, Long id) {
        Paciente paciente = obtenerPacienteActivoOException(id);
        log.info("Actualizando al paciente con id {}", id);
        
        pacienteTieneCitasAsignadas(id);
        validarUnicidadId(request, id);

        paciente.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad(),
                request.peso(),
                request.estatura(),
                request.email(),
                request.telefono(),
                request.direccion()
        );
        
        paciente.setImc(paciente.obtenerImc());
        paciente.setNumExpediente(paciente.generarNumExpediente());
        
        log.info("Paciente con id {} actualizado correctamente", id);
        return pacienteMapper.entidadAResponse(paciente);
    }

    @Override
    public void eliminar(Long id) {
    	Paciente paciente = obtenerPacienteActivoOException(id);
    	
    	pacienteTieneCitasAsignadas(id);
    	log.info("Eliminando al paciente con id {}", id);
    	
    	paciente.eliminar();
    	log.info("El paciente con id {} ha sido eliminado", id);
    }

    private Paciente obtenerPacienteActivoOException(Long id) {
        log.info("Buscando Paciente con id: {}", id);
        return pacienteRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(
                () -> new RecursoNoEncontradoException("Paciente no encontrado con id: " + id));
    }

    private void validarUnicidad(PacienteRequest request){

        if (pacienteRepository.existsByEmailIgnoreCaseAndEstadoRegistro
                (request.email(), EstadoRegistro.ACTIVO))
            throw new IllegalArgumentException("Ya existe un paciente registrado con el email: " + request.email());

        if (pacienteRepository.existsByTelefonoAndEstadoRegistro
                (request.telefono(), EstadoRegistro.ACTIVO))
            throw new RecursoNoEncontradoException("Ya existe un paciente registrado con el teléfono: " + request.telefono());
    }

    private void validarUnicidadId(PacienteRequest request, Long id){
        Paciente paciente = obtenerPacienteActivoOException(id);


        if (pacienteRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot
                (request.email(), paciente.getEstadoRegistro(), id))
            throw new IllegalArgumentException("Ya existe un paciente registrado con el email: " + request.email());

        if (pacienteRepository.existsByTelefonoAndEstadoRegistroAndIdNot
                (request.telefono(), paciente.getEstadoRegistro(), id))
            throw new IllegalArgumentException("Ya existe un paciente registrado con el teléfono: " + request.telefono());
    }
    
    private void pacienteTieneCitasAsignadas(Long id) {
    	citaClient.pacienteTieneCitasAsignadas(id);
    }
}
