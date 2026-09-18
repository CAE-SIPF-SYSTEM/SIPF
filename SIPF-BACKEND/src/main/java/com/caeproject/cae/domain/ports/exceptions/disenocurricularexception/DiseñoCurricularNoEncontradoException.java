package com.caeproject.cae.domain.ports.exceptions.disenocurricularexception;

public class DiseñoCurricularNoEncontradoException extends RuntimeException {
    public DiseñoCurricularNoEncontradoException(Long id, Long aLong) {
        super("Diseño curricular no encontrado con el ID: " + id);
    }
}
