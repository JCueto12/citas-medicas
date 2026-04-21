package com.josealberto.medicos.mappers;

import com.josealberto.commons.dto.MedicoRequest;
import com.josealberto.commons.dto.MedicoResponse;
import com.josealberto.medicos.entities.Medico;
import com.josealberto.commons.enums.EstadoRegistro;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper {

        public Medico requestAEntidad (MedicoRequest request) {
            if(request == null) return null;

            return Medico.builder()
                    .nombre(request.nombre())
                    .apellidoPaterno(request.apellidoPaterno())
                    .apellidoMaterno(request.apellidoMaterno())
                    .edad(request.edad())
                    .email(request.email())
                    .telefono(request.telefono())
                    .cedulaProfesional(request.cedulaProfesional())
                    .estadoRegistro(EstadoRegistro.ACTIVO)
                    .build();
        }

        public MedicoResponse entidadAResponse (Medico entidad) {
            if(entidad == null) return null;

            return new MedicoResponse(
                    entidad.getId(),
                    String.join(" ",
                            entidad.getNombre(),
                            entidad.getApellidoPaterno(),
                            entidad.getApellidoMaterno()),
                    entidad.getEdad(),
                    entidad.getEmail(),
                    entidad.getTelefono(),
                    entidad.getCedulaProfesional(),
                    entidad.getEspecialidad().getDescripcion(),
                    entidad.getDisponibilidad().getDescripcion()
            );
        }
}
