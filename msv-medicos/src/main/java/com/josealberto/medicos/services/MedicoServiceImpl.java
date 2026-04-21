package com.josealberto.medicos.services;

import com.josealberto.medicos.entities.Medico;
import com.josealberto.medicos.mappers.MedicoMapper;
import com.josealberto.medicos.repositories.MedicoRepository;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.josealberto.commons.dto.MedicoRequest;
import com.josealberto.commons.dto.MedicoResponse;
import com.josealberto.commons.enums.DisponibilidadMedico;
import com.josealberto.commons.enums.EspecialidadMedico;
import com.josealberto.commons.enums.EstadoRegistro;
import com.josealberto.commons.exceptions.RecursoNoEncontradoException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class MedicoServiceImpl implements MedicoService {
	
	private final MedicoRepository medicoRepository;
	private final MedicoMapper medicoMapper;


@Override
	public List<MedicoResponse> listar() {
		log.info("Listando todos los medicos activos");
		return medicoRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map(medicoMapper::entidadAResponse).toList();
	}

	@Override
	public MedicoResponse obtenerPorId(Long id) {
		
       return medicoMapper.entidadAResponse(obtenerMedicoActivoOException(id));
	}

	@Override
	public MedicoResponse registrar(MedicoRequest request) {
		if (request == null) return null;
        log.info("Registrando nuevo Médico...");

        valorarUnicos(request);

        Medico medico = medicoMapper.requestAEntidad(request);
        medico.setEspecialidad(
                EspecialidadMedico.obtenerEspecialidadPorCodigo(request.idEspecialidad()));

        medico.setDisponibilidad(DisponibilidadMedico.DISPONIBLE);
        log.info("Médico {} registrado con éxito", request.nombre());

        medicoRepository.save(medico);
        return medicoMapper.entidadAResponse(medico);
	}

	@Override
	public MedicoResponse actualizar(MedicoRequest request, Long id) {
		Medico medico = obtenerMedicoActivoOException(id);
		log.info("Actualizando al médico con id {}", id);
		
        valorarUnicosId(request, id);

        medico.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.edad(),
                request.email(),
                request.telefono(),
                request.cedulaProfesional(),
                EspecialidadMedico.obtenerEspecialidadPorCodigo(request.idEspecialidad())
        );
        log.info("Médico con id {} actualizadoo con éxito", id);

        return medicoMapper.entidadAResponse(medico);
	}

	@Override
	public void eliminar(Long id) {
		Medico medico = obtenerMedicoActivoOException(id);
		log.info("Eliminando al médico con id {}", id);
		
		medico.eliminar();
		log.info("Médico con id {} ha sido eliminado", id);
	}

	@Override
	public MedicoResponse obtenerPorIdTodos(Long id) {
		log.info("Buscando el médico con id: {}", id);
		 return medicoMapper.entidadAResponse(medicoRepository.findById(id).orElseThrow(
	                () -> new RecursoNoEncontradoException("Médico sin estado no encontrado con el id: " + id)));
	}

	@Override
	public void actualizarEstado(Long idMedico, Long idEspecialidad) {
		Medico medico = obtenerMedicoActivoOException(idMedico);
		
		
		DisponibilidadMedico nuevadisponibilidad = DisponibilidadMedico.obtenerDisponibilidadPorCodigo(idEspecialidad);
		
		if(medico.getDisponibilidad() == nuevadisponibilidad) return;
		
		DisponibilidadMedico anteriorDisponibilidad = medico.getDisponibilidad();
		
		medico.setDisponibilidad(nuevadisponibilidad);
		
		log.info("Disponibilidad del Médico con id {} cambio de {} a {}",
				idMedico, anteriorDisponibilidad, nuevadisponibilidad);
		
	}
	
	 
	 private Medico obtenerMedicoActivoOException(Long id) {
	        log.info("Buscando médico activo con id: {}", id);
	        return medicoRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(
	                () -> new RecursoNoEncontradoException("Médico activo no encontrado con id: " + id));
	    }


	    private void valorarUnicos(MedicoRequest request) {

	        if ( medicoRepository.existsByEmailAndEstadoRegistro(
	                request.email().toLowerCase(), EstadoRegistro.ACTIVO))
	            throw new IllegalArgumentException("Ya existe un médico con el email: " + request.email());

	        if ( medicoRepository.existsByTelefonoAndEstadoRegistro(
	                request.telefono(), EstadoRegistro.ACTIVO))
	            throw new IllegalArgumentException(
	                    "Ya existe un médico con el telefono: " + request.telefono());

	        if (medicoRepository.existsByCedulaProfesionalAndEstadoRegistro(
	                request.cedulaProfesional(), EstadoRegistro.ACTIVO))
	            throw new IllegalArgumentException(
	                    "Ya existe un medico registrado con la cedula: " + request.cedulaProfesional());
	    }


	    private void valorarUnicosId(MedicoRequest request, Long id) {
	        Medico medico = obtenerMedicoActivoOException(id);

	        if ( medico.getEstadoRegistro() == EstadoRegistro.ELIMINADO)
	            return;

	        if ( !medico.getEmail().equalsIgnoreCase(request.email()) &&
	        medicoRepository.existsByEmailAndEstadoRegistroAndIdNot(
	                request.email().toLowerCase(), medico.getEstadoRegistro(), id))
	            throw new IllegalArgumentException("Ya existe un médico con el email {}" + request.email());

	        if ( !medico.getTelefono().equalsIgnoreCase(request.telefono()) &&
	                medicoRepository.existsByTelefonoAndEstadoRegistroAndIdNot(
	                request.telefono(), medico.getEstadoRegistro(), id) )
	            throw new IllegalArgumentException(
	                    "Ya existe un médico con el telefono {}" + request.telefono());

	        if ( !medico.getCedulaProfesional().equalsIgnoreCase(request.cedulaProfesional()) &&
	                medicoRepository.existsByCedulaProfesionalAndIdNotAndEstadoRegistro(
	                        request.cedulaProfesional(), id, medico.getEstadoRegistro()))
	            throw new IllegalArgumentException(
	                    "Ya existe un medico registrado con la cedula: " + request.cedulaProfesional());
	    }

}
