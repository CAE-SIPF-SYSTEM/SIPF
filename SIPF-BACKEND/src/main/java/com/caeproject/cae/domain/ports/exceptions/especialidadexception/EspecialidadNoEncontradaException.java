package com.caeproject.cae.domain.ports.exceptions.especialidadexception;

public class EspecialidadNoEncontradaException extends RuntimeException {
    public EspecialidadNoEncontradaException(Long id) {
        super("No se encontró la especialidad con el ID: " + id);
    }
}
