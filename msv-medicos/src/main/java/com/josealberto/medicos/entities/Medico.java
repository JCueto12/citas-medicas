package com.josealberto.medicos.entities;


import com.josealberto.commons.enums.DisponibilidadMedico;
import com.josealberto.commons.enums.EspecialidadMedico;
import com.josealberto.commons.enums.EstadoRegistro;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MEDICOS")
@Builder
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EDAD", nullable = false)
    @Min(value = 18, message = "El médico debe ser mayor de edad (mínimo 18 años)")
    @Max(value = 100, message = "La edad no puede ser mayor a 100 años")
    private Integer edad;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "TELEFONO", nullable = false, length = 10)
    private String telefono;

    @Column(name = "CEDULA_PROFESIONAL", nullable = false, length = 12)
    private String cedulaProfesional;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESPECIALIDAD",  nullable = false, length = 30)
    private EspecialidadMedico especialidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISPONIBILIDAD", nullable = false,  length = 30)
    private DisponibilidadMedico disponibilidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false,  length = 30)
    private EstadoRegistro estadoRegistro;

    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno, Integer edad, String email, String telefono, String cedulaProfesional, EspecialidadMedico especialidad) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
        this.email = email;
        this.telefono = telefono;
        this.cedulaProfesional = cedulaProfesional;
        this.especialidad = especialidad;
    }
    
    public void eliminar() {
    	this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }
}
