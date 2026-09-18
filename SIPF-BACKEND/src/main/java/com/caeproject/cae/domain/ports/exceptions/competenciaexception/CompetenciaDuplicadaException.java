package com.caeproject.cae.domain.ports.exceptions.competenciaexception;

public class CompetenciaDuplicadaException extends RuntimeException {
    public CompetenciaDuplicadaException(String codigo) {
        super("Ya existe una competencia registrada con el código: " + codigo);
    }
}
