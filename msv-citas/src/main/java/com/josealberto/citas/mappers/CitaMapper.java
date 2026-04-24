package com.josealberto.citas.mappers;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.josealberto.citas.dto.CitaRequest;
import com.josealberto.citas.dto.CitaResponse;
import com.josealberto.citas.entities.Cita;
import com.josealberto.citas.enums.EstadoCita;
import com.josealberto.commons.dto.DatosMedico;
import com.josealberto.commons.dto.DatosPaciente;
import com.josealberto.commons.dto.MedicoResponse;
import com.josealberto.commons.dto.PacienteResponse;
import com.josealberto.commons.enums.EstadoRegistro;
import com.josealberto.commons.mappers.CommonMapper;

@Component
public class CitaMapper implements CommonMapper<CitaRequest, CitaResponse, Cita> {

	@Override
	public Cita requestAEntidad(CitaRequest request) {
		if(request == null) return null;
		
		return Cita.builder()
				.idPaciente(request.idPaciente())
				.idMedico(request.idMedico())
				.fechaCita(request.fechaCita())
				.sintomas(request.sintomas())
				.estadoCita(EstadoCita.PENDIENTE)
				.estadoRegistro(EstadoRegistro.ACTIVO)
				.build();
	}

	@Override
	public CitaResponse entidadAResponse(Cita entidad) {
		if(entidad == null) return null;
		
		return new CitaResponse(
				entidad.getId(),
				null,
				null,
				entidad.getFechaCita(),
				entidad.getSintomas(),
				entidad.getEstadoCita().getDescripcion());
	}
	
	public CitaResponse entidadAResponse(Cita entidad, PacienteResponse paciente, MedicoResponse medico) {
		if(entidad == null) return null;
		
		return new CitaResponse(
				entidad.getId(),
				this.pacienteResponseADatosPaciente(paciente),
				this.medicoResponseADatosMedico(medico),
				entidad.getFechaCita(),
				entidad.getSintomas(),
				entidad.getEstadoCita().getDescripcion());
	}
	
	private DatosPaciente pacienteResponseADatosPaciente(PacienteResponse paciente) {
		if(paciente == null) return null;
		
		return new DatosPaciente(
				paciente.nombre(),
				paciente.numExpediente(),
				paciente.edad() + " años",
				paciente.peso() + " kg",
				paciente.estatura() + " m.",
				String.join(" ", paciente.imc().toString(),
						this.clasificacionIMC(paciente.imc())),
				paciente.telefono()
		);
				
	}
	
	private DatosMedico medicoResponseADatosMedico(MedicoResponse medico) {
		if(medico == null) return null;
		
		return new DatosMedico(
				medico.nombre(),
				medico.cedulaProfesional(),
				medico.especialidad()
		);
	}
	
	private String clasificacionIMC(BigDecimal imc) {
		 if (imc.compareTo(new BigDecimal("18.5")) < 0) return "Bajo peso";
		 if (imc.compareTo(new BigDecimal("25")) < 0) return "Normal";
		 if (imc.compareTo(new BigDecimal("30")) < 0) return "Sobrepeso";
		 if (imc.compareTo(new BigDecimal("35")) < 0) return "Obesidad grado I";
		 if (imc.compareTo(new BigDecimal("40")) < 0) return "Obesidad grado II";
		 return "Obesidad grado III";
	}

}
