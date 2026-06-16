package com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException;

public class FichaDuplicadaException extends RuntimeException {
    public FichaDuplicadaException(String codigoFicha) {
        super(String.format("Ya existe una ficha registrada con el código alfanumérico: %s", codigoFicha));
    }
}
