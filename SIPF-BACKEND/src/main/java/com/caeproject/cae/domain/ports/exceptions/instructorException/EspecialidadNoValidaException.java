package com.caeproject.cae.domain.ports.exceptions.instructorException;

public class EspecialidadNoValidaException extends RuntimeException {
    public EspecialidadNoValidaException(Long usuarioId, String nombreCompetencia) {
        super(String.format("El instructor %d no cuenta con la especialidad técnica requerida para impartir la competencia: %s.",
                usuarioId, nombreCompetencia));
    }
}
