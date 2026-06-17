package com.caeproject.cae.domain.ports.exceptions.sessionExceptions;

public class ContrasenaInvalidaException extends RuntimeException {
    public ContrasenaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
