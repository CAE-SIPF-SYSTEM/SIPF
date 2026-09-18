package com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception;

public class DisponibilidadYaRegistradaException extends RuntimeException {
    public DisponibilidadYaRegistradaException(Long usuarioId) {
        super("El instructor con ID " + usuarioId + " ya tiene una disponibilidad registrada. Utilice la función de editar.");
    }
}
