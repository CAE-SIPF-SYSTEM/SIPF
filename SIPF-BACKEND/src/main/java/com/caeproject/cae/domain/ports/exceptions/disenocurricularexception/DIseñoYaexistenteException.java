package com.caeproject.cae.domain.ports.exceptions.diseñocurricularexception;

public class DIseñoYaexistenteException extends RuntimeException {
    public DIseñoYaexistenteException(Long programaId) {
        super("El diseño curricular ya existe para el RAP con ID: " + programaId);
    }
}
