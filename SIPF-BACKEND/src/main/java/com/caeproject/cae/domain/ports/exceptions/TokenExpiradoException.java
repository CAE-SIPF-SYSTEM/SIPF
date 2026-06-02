package com.caeproject.cae.domain.ports.exceptions;

public class TokenExpiradoException extends RuntimeException {

    public TokenExpiradoException() {
        super("El token de recuperación ha expirado. Solicite uno nuevo.");
    }
}
