package com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException;

public class FichaNoEncontradaException extends RuntimeException {
    public FichaNoEncontradaException(Long fichaid) {
        super ("El programa con  " +  fichaid +  " no fue encontrado" );
    }
}
