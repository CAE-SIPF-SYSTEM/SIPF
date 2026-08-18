package com.caeproject.cae.domain.ports.exceptions.asignacionexceptions;

public class CruceHorarioException extends RuntimeException {
    private final String fichaCodigo;

    public CruceHorarioException(String mensaje, String fichaCodigo) {
        super(mensaje);
        this.fichaCodigo = fichaCodigo;
    }

    public String getFichaCodigo() {
        return fichaCodigo;
    }
}
