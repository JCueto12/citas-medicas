package com.josealberto.pacientes.mappers;

import com.josealberto.commons.dto.PacienteRequest;
import com.josealberto.commons.dto.PacienteResponse;
import com.josealberto.pacientes.entities.Paciente;
import com.josealberto.commons.enums.EstadoRegistro;
import org.springframework.stereotype.Component;


@Component
public class PacienteMapper {

        public Paciente requestAEntidad(PacienteRequest request) {
            if (request == null) return null;

            return Paciente.builder()
                    .nombre(request.nombre())
                    .apellidoPaterno(request.apellidoPaterno())
                    .apellidoMaterno(request.apellidoMaterno())
                    .edad(request.edad())
                    .peso(request.peso())
                    .estatura(request.estatura())
                    .email(request.email().toLowerCase())
                    .telefono(request.telefono())
                    .direccion(request.direccion())
                    .estadoRegistro(EstadoRegistro.ACTIVO)
                    .build();
            }

        public PacienteResponse entidadAResponse(Paciente entidad) {
            if (entidad == null) return null;

            return new PacienteResponse(
                    entidad.getId(),
                    String.join(" ",
                            entidad.getNombre(),
                            entidad.getApellidoPaterno(),
                            entidad.getApellidoMaterno()),
                    entidad.getEdad(),
                    entidad.getPeso(),
                    entidad.getEstatura(),
                    entidad.getImc(),
                    entidad.getEmail(),
                    entidad.getTelefono(),
                    entidad.getDireccion(),
                    entidad.getNumExpediente()
            );
        }

}
