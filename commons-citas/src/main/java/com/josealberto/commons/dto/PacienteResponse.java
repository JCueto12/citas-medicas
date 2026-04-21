package com.josealberto.commons.dto;

import java.math.BigDecimal;

public record PacienteResponse(
        Long id,
        String nombre,
        Integer edad,
        BigDecimal peso,
        BigDecimal estatura,
        BigDecimal imc,
        String email,
        String telefono,
        String direccion,
        String numExpediente
) {
}
