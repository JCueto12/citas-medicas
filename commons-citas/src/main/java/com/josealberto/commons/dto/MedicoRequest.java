package com.josealberto.commons.dto;

import jakarta.validation.constraints.*;

public record MedicoRequest(

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
        @Min(value = 18, message = "El médico debe ser mayor de edad")
        @Max(value = 100)
        Integer edad,

        @NotBlank(message = "El email es requerido")
        @Email(message = "Formato de email inválido")
        @Size(min = 1, max = 100, message = "El email debe tener máximo 100 caracteres")
        String email,

        @NotBlank(message = "El telefono es requerido")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos numéricos")
        String telefono,

        @NotNull(message = "La edad es requerida")
        @Size(min = 12, max = 12, message = "La cedula profesional debe tener exactamente 12 caracteres")
        String cedulaProfesional,

        @NotNull(message = "El ID de la aula es obligatorio")
        Long idEspecialidad

) {
}
