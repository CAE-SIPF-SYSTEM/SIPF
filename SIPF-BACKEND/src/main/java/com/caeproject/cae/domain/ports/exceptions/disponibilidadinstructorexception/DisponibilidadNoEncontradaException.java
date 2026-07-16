package com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception;

public class DisponibilidadNoEncontradaException extends RuntimeException {
    public DisponibilidadNoEncontradaException(Long usuarioId) {
        super("No se encontró registro de disponibilidad para el instructor con ID: " + usuarioId);
    }
}
