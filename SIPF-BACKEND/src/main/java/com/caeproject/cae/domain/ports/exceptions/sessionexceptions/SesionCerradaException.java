package com.caeproject.cae.domain.ports.exceptions.sessionexceptions;

public class SesionCerradaException extends RuntimeException {
    public SesionCerradaException( ) {
        super("Tu sesión ha sido cerrada debido a otro inicio de sesión");
    }
}
