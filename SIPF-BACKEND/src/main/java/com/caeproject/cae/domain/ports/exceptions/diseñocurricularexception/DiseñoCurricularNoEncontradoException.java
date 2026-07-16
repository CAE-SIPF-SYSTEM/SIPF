package com.caeproject.cae.domain.ports.exceptions.diseñocurricularexception;

public class DiseñoCurricularNoEncontradoException extends RuntimeException {
    public DiseñoCurricularNoEncontradoException(Long id) {
        super("Diseño curricular no encontrado con el ID: " + id);
    }
}
