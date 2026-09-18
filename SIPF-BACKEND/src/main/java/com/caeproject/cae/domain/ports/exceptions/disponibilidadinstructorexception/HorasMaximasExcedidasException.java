package com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception;

public class HorasMaximasExcedidasException extends RuntimeException {
    public HorasMaximasExcedidasException(Long usuarioId, Long horasAsignadas, Long horasMaximas) {
        super("No se puede asignar el RAP. El instructor " + usuarioId + " ya tiene " + horasAsignadas + " horas asignadas y su límite máximo es de " + horasMaximas + " horas.");
    }
}
