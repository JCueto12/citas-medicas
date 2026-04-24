package com.josealberto.auth.services;

import java.util.Set;

import com.josealberto.auth.dto.UsuarioRequest;
import com.josealberto.auth.dto.UsuarioResponse;

public interface UsuarioService {

    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}

