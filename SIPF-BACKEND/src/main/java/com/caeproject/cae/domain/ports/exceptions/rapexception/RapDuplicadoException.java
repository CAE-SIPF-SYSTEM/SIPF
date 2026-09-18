package com.caeproject.cae.domain.ports.exceptions.rapexception;

public class RapDuplicadoException extends RuntimeException {
    public RapDuplicadoException(String nombre) {
        super("Ya existe un RAP registrado con el nombre: " + nombre);
    }
}
