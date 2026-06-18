package com.caeproject.cae.domain.ports.exceptions.rapexception;

public class RapNoEncontradoException extends RuntimeException {
    public RapNoEncontradoException(Long id) {
        super("El RAP con ID " + id + " no fue encontrado.");
    }
}
