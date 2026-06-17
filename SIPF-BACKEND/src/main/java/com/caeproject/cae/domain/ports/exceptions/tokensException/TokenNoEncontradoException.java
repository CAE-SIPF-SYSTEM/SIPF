package com.caeproject.cae.domain.ports.exceptions.tokensException;

public class TokenNoEncontradoException extends RuntimeException {

    public TokenNoEncontradoException() {
        super("El token de recuperación no es válido.");
    }
}
