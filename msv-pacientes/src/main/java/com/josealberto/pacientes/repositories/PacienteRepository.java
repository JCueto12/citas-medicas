package com.josealberto.pacientes.repositories;

import com.josealberto.pacientes.entities.Paciente;
import com.josealberto.commons.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    List<Paciente> findByEstadoRegistro(EstadoRegistro estadoRegistro);
    
    Optional<Paciente> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndEstadoRegistro(String email, EstadoRegistro estado);

    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estado);

    boolean existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
            String email, EstadoRegistro estado, Long id);

    boolean existsByTelefonoAndEstadoRegistroAndIdNot(
            String telefono, EstadoRegistro estado, Long id);
}
