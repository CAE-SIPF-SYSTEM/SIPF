package com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception;

public class FichaNoEncontradaException extends RuntimeException {
    public FichaNoEncontradaException(Long fichaid) {
        super ("El programa con  " +  fichaid +  " no fue encontrado" );
    }
}
