package com.josealberto.commons.dto;

public record ErrorResponse(
        int codigo,
        String mensaje
) {
}
