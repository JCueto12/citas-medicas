package com.josealberto.citas.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.josealberto.citas.entities.Cita;
import com.josealberto.citas.enums.EstadoCita;
import com.josealberto.commons.enums.EstadoRegistro;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
	
	List<Cita> findByEstadoRegistro(EstadoRegistro estadoRegistro);
	
	Optional<Cita> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);
	
	boolean existsByIdMedicoAndEstadoRegistro(Long idMedico, EstadoRegistro estadoRegistro);
	
	boolean existsByIdPacienteAndEstadoRegistro(Long idPaciente, EstadoRegistro estadoRegistro);
	
	boolean existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn
		(Long idMedico, EstadoRegistro estadoRegistro, List<EstadoCita> estadosCita);
	
	boolean existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn
		(Long idPaciente, EstadoRegistro estadoRegistro, List<EstadoCita> estadosCita);
	
	boolean existsByIdPacienteAndEstadoCitaInAndIdNot(Long idPaciente, List<EstadoCita> estadoCita, Long idCita);
	
	boolean existsByIdMedicoAndEstadoCitaInAndIdNot(Long idMedico, List<EstadoCita> estadoCita, Long idCita);
}
