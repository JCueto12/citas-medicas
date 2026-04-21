package com.josealberto.commons.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record PacienteRequest(

        @NotBlank(message = "El nombre es necesario")
        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracterés")
        String nombre,

        @NotBlank(message = "El nombre es necesario")
        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracterés")
        String apellidoPaterno,

        @NotBlank(message = "El nombre es necesario")
        @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracterés")
        String apellidoMaterno,

        @NotNull(message = "La edad es requerida")
        @Min(value = 1, message = "El paciente debe tener al menos 1 año")
        @Max(value = 100)
        Integer edad,

        @NotNull(message = "El peso es requerido")
        @DecimalMin(value = "0.1", message = "El peso debe ser positivo")
        @DecimalMax(value = "200", message = "El pero máximo es de 200 kgs")
        BigDecimal peso,

        @NotNull(message = "La estatura es requerida")
        @DecimalMin(value = "1.0", message = "La estatura mínima es de 1 metro")
        @DecimalMax(value = "2.0", message = "La estatura máxima es de 2 metros")
        BigDecimal estatura,

        @NotBlank(message = "El email es requerido")
        @Email(message = "Formato de email inválido")
        @Size(min = 1, max = 100, message = "El email debe tener máximo 100 caracteres")
        String email,

        @NotBlank(message = "El telefono es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos numéricos")
        String telefono,

        @NotNull(message = "La dirección es requerida")
        @Size(min = 1, max = 150, message = "La dirección debe tener entre 1 y 150 caracteres")
        String direccion
) {
}
