package com.caeproject.cae.domain.ports.exceptions.competenciaexception;

public class CompetenciaNoEncontradaException extends RuntimeException {
    public CompetenciaNoEncontradaException(Long id) {
        super("La competencia con ID " + id + " no fue encontrada.");
    }
}
