package com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException;

public class ProgramaDuplicadoException extends RuntimeException {
    public ProgramaDuplicadoException(String name) {
        super ("El programa con  " +  name +  " no fue encontrado" );
    }
}
