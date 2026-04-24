package com.josealberto.auth.services;

import com.josealberto.auth.dto.LoginRequest;
import com.josealberto.auth.dto.TokenResponse;

public interface AuthService {

    TokenResponse autenticar(LoginRequest request) throws Exception;
}

