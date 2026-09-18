package com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception;

public class DiaNoDisponibleException extends RuntimeException {
    public DiaNoDisponibleException(Long usuarioId, String diaSolicitado) {
        super("El instructor con ID " + usuarioId + " no tiene disponibilidad para el día: " + diaSolicitado);
    }
}
