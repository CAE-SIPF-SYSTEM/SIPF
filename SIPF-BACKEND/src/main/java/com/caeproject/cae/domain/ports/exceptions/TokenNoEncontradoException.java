package com.caeproject.cae.domain.ports.exceptions;

public class TokenNoEncontradoException extends RuntimeException {

    public TokenNoEncontradoException() {
        super("El token de recuperación no es válido.");
    }
}
