package com.josealberto.pacientes.entities;


import com.josealberto.commons.enums.EstadoRegistro;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PACIENTES")
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    private String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    private String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    private String apellidoMaterno;

    @Column(name = "EDAD", nullable = false)
    @Min(value = 1, message = "El paciente debe tener un año mínimo")
    @Max(value = 100, message = "La edad no puede ser mayor a 100 años")
    private Integer edad;

    @Column(name = "PESO", nullable = false)
    @DecimalMin(value = "0.1", message = "El peso debe ser positivo")
    @DecimalMax(value = "200", message = "El pero máximo es de 200 kgs")
    private BigDecimal peso;

    @Column(name = "ESTATURA", nullable = false)
    @DecimalMin(value = "1.0", message = "La estatura mínima es de 1 metro")
    @DecimalMax(value = "2.0", message = "La estatura máxima es de 2 metros")
    private BigDecimal estatura;

    @Column(name = "IMC", nullable = false)
    @DecimalMin(value = "10.0", message = "El IMC debe ser al menos de 10")
    @DecimalMax(value = "50.0", message = "El IMC debe ser máximo de 50")
    private BigDecimal imc;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "NUM_EXPEDIENTE", nullable = false, length = 20)
    private String numExpediente;

    @Column(name = "TELEFONO", nullable = false, length = 10)
    private String telefono;

    @Column(name = "DIRECCION", nullable = false, length = 150)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    private EstadoRegistro estadoRegistro;

    public void actualizar(String nombre, String apellidoPaterno, String apellidoMaterno, Integer edad, BigDecimal peso, BigDecimal estatura, String email, String telefono, String direccion) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
        this.peso = peso;
        this.estatura = estatura;
        this.imc = obtenerImc();
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.numExpediente = generarNumExpediente();
    }
    
    public BigDecimal obtenerImc() {

        BigDecimal estaturaAlCuadrado = this.estatura.multiply(estatura);

        return this.peso.divide(estaturaAlCuadrado, 2, RoundingMode.HALF_UP);
    }

    public String generarNumExpediente(){
        StringBuilder expediente = new StringBuilder();

        for (char c : this.telefono.toCharArray())
        	expediente.append(c).append("X");
        
        return expediente.toString();
    }
    
    public void eliminar() {
    	this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }
}
