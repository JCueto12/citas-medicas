package com.josealberto.auth.dto;

public record ErrorResponse(
        int codigo,
        String mensaje
) { }

