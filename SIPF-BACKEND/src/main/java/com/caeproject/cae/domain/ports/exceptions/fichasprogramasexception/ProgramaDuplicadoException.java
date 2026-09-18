package com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception;

public class ProgramaDuplicadoException extends RuntimeException {
    public ProgramaDuplicadoException(String name) {
        super ("El programa con  " +  name +  " no fue encontrado" );
    }
}
