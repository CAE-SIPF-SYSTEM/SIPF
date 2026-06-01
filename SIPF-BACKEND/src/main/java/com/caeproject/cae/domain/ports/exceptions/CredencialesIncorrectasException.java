package com.caeproject.cae.domain.ports.exceptions;

public class CredencialesIncorrectasException extends RuntimeException {
    public CredencialesIncorrectasException() {
        super("Las credenciales son incorrectas, inténtelo de nuevo.");
    }
}
