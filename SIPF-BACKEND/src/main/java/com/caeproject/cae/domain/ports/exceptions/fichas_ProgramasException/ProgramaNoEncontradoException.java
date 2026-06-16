package com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException;

public class ProgramaNoEncontradoException extends RuntimeException {
    public ProgramaNoEncontradoException(Long programaid) {
        super ("El programa con  " +  programaid +  " no fue encontrado" );
    }
}
