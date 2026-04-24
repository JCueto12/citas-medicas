package com.josealberto.citas.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.josealberto.citas.dto.CitaRequest;
import com.josealberto.citas.dto.CitaResponse;
import com.josealberto.citas.entities.Cita;
import com.josealberto.citas.enums.EstadoCita;
import com.josealberto.citas.mappers.CitaMapper;
import com.josealberto.citas.repositories.CitaRepository;
import com.josealberto.commons.clients.MedicoClient;
import com.josealberto.commons.clients.PacienteClient;
import com.josealberto.commons.dto.MedicoResponse;
import com.josealberto.commons.dto.PacienteResponse;
import com.josealberto.commons.enums.DisponibilidadMedico;
import com.josealberto.commons.enums.EstadoRegistro;
import com.josealberto.commons.exceptions.EntidadRelacionadaException;
import com.josealberto.commons.exceptions.RecursoNoEncontradoException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CitaServiceImpl implements CitaService {
	
	private final CitaRepository citaRepository;
	private final CitaMapper citaMapper;
	private final MedicoClient medicoCliente;
	private final PacienteClient pacienteClient;
	private final  List<EstadoCita> ESTADOS_INVALIDOS_CREACION = 
			List.of(EstadoCita.CONFIRMADA, EstadoCita.EN_CURSO, EstadoCita.PENDIENTE);
	
	
	@Override
	@Transactional(readOnly = true)
	public List<CitaResponse> listar() {
		log.info("Listando las citas ...");
		return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
				.map( cita -> 
					citaMapper.entidadAResponse(
							cita,
							obtenerPacienteSinEstado(cita.getIdPaciente()), 
							obtenerMedicoSinEstado(cita.getIdMedico()))
				).toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public CitaResponse obtenerPorId(Long id) {
		Cita cita = obtenerCitaActivaOException(id);
		return citaMapper.entidadAResponse(cita,
				obtenerPacienteSinEstado(cita.getIdPaciente()),
				obtenerMedicoSinEstado(cita.getIdMedico()));
	}
	
	@Override
	@Transactional(readOnly = true)
	public void medicoTieneCitasAsignadas(Long idMedico) {
		log.info("Buscando citas asignadas en estado {} o {} para el médico con id: {}",
				ESTADOS_INVALIDOS_CREACION, idMedico);
		boolean tieneCitas = citaRepository.existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn
				(idMedico, EstadoRegistro.ACTIVO, ESTADOS_INVALIDOS_CREACION);
		
		if(tieneCitas)
			throw new EntidadRelacionadaException("No se puede modificar al médico ya que tiene citas con estado: " + ESTADOS_INVALIDOS_CREACION);
	}

	@Override
	@Transactional(readOnly = true)
	public void pacienteTieneCitasAsignadas(Long idPaciente) {
		log.info("Buscando citas asignadas en estado {} o {} para el médico con id: {}",
				ESTADOS_INVALIDOS_CREACION, idPaciente);
		
		boolean tieneCitas = citaRepository.existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn
				(idPaciente, EstadoRegistro.ACTIVO, ESTADOS_INVALIDOS_CREACION);
		
		if(tieneCitas)
			throw new EntidadRelacionadaException(
					"No se puede modificar al médico ya que tiene citas con estado: " + ESTADOS_INVALIDOS_CREACION);
		
	}
	
	@Override
	public CitaResponse registrar(CitaRequest rq) {
		log.info("Registrando alumno nuevo...");
		
		
		// esto valida que el id del medico este activo en el micorservicio de medico
		MedicoResponse medico = obtenerMedicoActivo(rq.idMedico());
		validarDisponibilidadMedico(medico);
		PacienteResponse paciente = obtenerPacienteActivo(rq.idPaciente());
		
		validarMedico(rq.idMedico(), ESTADOS_INVALIDOS_CREACION);
		validaPaciente(rq.idPaciente(), ESTADOS_INVALIDOS_CREACION);
		
		
		Cita cita = citaRepository.save(citaMapper.requestAEntidad(rq));
		cambiarDisponibilidadSegunEstadoCita(rq.idMedico(), cita.getEstadoCita());
		
		return citaMapper.entidadAResponse(cita, paciente, medico);
	}
	
	@Override
	public CitaResponse actualizar(CitaRequest request, Long id) {
		Cita cita = obtenerCitaActivaOException(id);
		log.info("Actualizando Cita con id: {}", cita.getId());
		
		if(cita.getEstadoCita() != EstadoCita.PENDIENTE)
			throw new IllegalStateException("Solo se puede actualizar una cita si se encuentra en estado de: " + EstadoCita.PENDIENTE);
		
		if( request.idEstadoCita() != null &&  request.idEstadoCita().equals(EstadoCita.CANCELADA.getCodigo())) {
			cita.actualizarEstadoCita(EstadoCita.obtenerEstadoCitaPorCodigo(request.idEstadoCita()));
			cambiarDisponibilidadSegunEstadoCita(cita.getIdMedico(), cita.getEstadoCita());
			return citaMapper.entidadAResponse(
					cita,
					obtenerPacienteActivo(request.idPaciente()),
					obtenerMedicoActivo(cita.getIdMedico()));
		}
		
		Long idMedico = cita.getIdMedico();			
		MedicoResponse medicoNuevo = cita.getIdMedico().equals(request.idMedico()) ? null : obtenerMedicoActivo(request.idMedico());
		PacienteResponse pacienteNuevo = cita.getIdPaciente().equals(request.idPaciente()) ? null : obtenerPacienteActivo(request.idPaciente());
		
		if( medicoNuevo != null )
			validarDisponibilidadMedico(medicoNuevo);

		
		if( pacienteNuevo != null)
			validarPacienteCitaExistente(pacienteNuevo.id(), id);
		
		cita.actualizar(
				request.idPaciente(),
				request.idMedico(),
				request.fechaCita(),
				request.sintomas());
		
		if( request.idEstadoCita() == EstadoCita.CONFIRMADA.getCodigo()) {
			EstadoCita estadoCita = EstadoCita.obtenerEstadoCitaPorCodigo(cita.getEstadoCita().getCodigo());	
			cita.actualizarEstadoCita(estadoCita);
		}
		
		if( medicoNuevo != null ) {
			cambiarDisponibilidadMedico(idMedico, DisponibilidadMedico.DISPONIBLE.getCodigo());
			cambiarDisponibilidadSegunEstadoCita(request.idMedico() , cita.getEstadoCita());
		}
		
		
		medicoNuevo = obtenerMedicoActivo(request.idMedico());
		pacienteNuevo = obtenerPacienteActivo(request.idPaciente());
		
		return citaMapper.entidadAResponse(cita, pacienteNuevo, medicoNuevo);
	}
	
	@Override
	public CitaResponse actualizarEstadoCita(Long idCita, Long idEstado) {
		Cita cita = obtenerCitaActivaOException(idCita);
		log.info("Actualizando el estado de la cita con id {}", idCita);
		
		EstadoCita estadoCita = EstadoCita.obtenerEstadoCitaPorCodigo(idEstado);
		cita.actualizarEstadoCita(estadoCita);
		
		log.info("El estado de la cita con id {} ha sido actualizado", idCita);
		cambiarDisponibilidadSegunEstadoCita(cita.getIdMedico(), cita.getEstadoCita());
		return citaMapper.entidadAResponse(cita);
	}
	
	@Override
	public void eliminar(Long id) {
		Cita cita = obtenerCitaActivaOException(id);
		log.info("Eliminando la cita con id {}", id);
		
		
		cita.eliminar();
		if( cita.getEstadoCita().equals(EstadoCita.PENDIENTE) )
			cambiarDisponibilidadMedico( 
					cita.getIdMedico(), DisponibilidadMedico.DISPONIBLE.getCodigo());
		
		log.info("La cita con id {} ha sido eliminada exitosamente");
	}
	
	
	
	private Cita obtenerCitaActivaOException(Long id) {
        log.info("Buscando cita activa con id: {}", id);
        return citaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO).orElseThrow(
                () -> new RecursoNoEncontradoException("Cita no encontrada con id: " + id));
    }
	
	private MedicoResponse obtenerMedicoActivo(Long id) {
        log.info("Buscando cita activa con id: {}", id);
        return medicoCliente.obtenerPorId(id);
    }
	
	private MedicoResponse obtenerMedicoSinEstado(Long id) {
        log.info("Buscando cita activa con id: {}", id);
        return medicoCliente.obtenerMedicoPorIdSinEstado(id);
    }
	
	private PacienteResponse obtenerPacienteActivo(Long id) {
        log.info("Buscando cita activa con id: {}", id);
        return pacienteClient.obtenerPorId(id);
    }
	
	private PacienteResponse obtenerPacienteSinEstado(Long id) {
        log.info("Buscando cita activa con id: {}", id);
        return pacienteClient.obtenerPorIdTodos(id);
    }
	
	
	private void validarMedico(Long idMedico, List<EstadoCita> lista) {
		log.info("Validando si el médico tiene una cita activa con los estados {}", lista);
		if(  citaRepository.existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn(
				idMedico, EstadoRegistro.ACTIVO, lista) )
			throw new EntidadRelacionadaException(
					"No se puede registrar la cita ya que el Médico ya cuenta con una cita activa con estados: " + lista);
		}
		
	private void validaPaciente(Long idPaciente, List<EstadoCita> lista) {
		log.info("Validando si el paciente tiene una cita activa con los estados {}", lista);
		if(  citaRepository.existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(
			idPaciente, EstadoRegistro.ACTIVO, ESTADOS_INVALIDOS_CREACION) )
			throw new EntidadRelacionadaException(
					"No se puede registrar la cita ya que el paciente ya cuenta con una cita activa con estados: " + lista);
	}
	
	private void validarDisponibilidadMedico(MedicoResponse medico) {
		log.info("Validadndo si el médico se encuentra en estado {}", DisponibilidadMedico.DISPONIBLE);
		if( !DisponibilidadMedico.DISPONIBLE.getDescripcion().equalsIgnoreCase(medico.disponibilidad()) )
			throw new IllegalStateException("El médico no se encuentra en estado: " + DisponibilidadMedico.DISPONIBLE);
	}
	
	private void cambiarDisponibilidadMedico(Long idMedico, Long idDisponibilidad) {
		log.info("Cambiando la disponibilidad del Médico con id {}", idMedico);
		medicoCliente.actualizarDisponibilidadMedico(idMedico, idDisponibilidad);
	}
	
	private void cambiarDisponibilidadSegunEstadoCita(Long idMedico, EstadoCita estadoCita) {
		switch(estadoCita) {
			case PENDIENTE, CONFIRMADA -> cambiarDisponibilidadMedico(idMedico, DisponibilidadMedico.NO_DISPONIBLE.getCodigo());
			case EN_CURSO -> cambiarDisponibilidadMedico(idMedico, DisponibilidadMedico.EN_CONSULTA.getCodigo());
			case CANCELADA, FINALIZADA -> cambiarDisponibilidadMedico(idMedico, DisponibilidadMedico.DISPONIBLE.getCodigo());
		}
	}
	
	private void validarPacienteCitaExistente(Long idPaciente, Long idCita) {
		if( citaRepository.existsByIdPacienteAndEstadoCitaInAndIdNot(idPaciente, ESTADOS_INVALIDOS_CREACION, idCita) )
			throw new EntidadRelacionadaException(
					"No se puede registrar la cita ya que el paciente ya cuenta con una cita activa con estados: " + ESTADOS_INVALIDOS_CREACION);
	}
	

}
