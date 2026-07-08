package com.caeproject.cae.domain.ports.exceptions.especialidadexception;

public class EspecialidadDuplicadaException extends RuntimeException {
    public EspecialidadDuplicadaException(String nombre) {
        super("Ya existe una especialidad registrada con el nombre: " + nombre);
    }
}
