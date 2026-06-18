package com.caeproject.cae.domain.ports.exceptions.instructorexception;

public class LimiteHorasSuperadasException extends RuntimeException {
    public LimiteHorasSuperadasException(Long usuarioId, Long horasMaximas) {
        super(String.format("El instructor con ID %d no puede ser asignado debido a que supera sus horas máximas semanales o mensuales (%d horas) según su contrato.",
                usuarioId, horasMaximas));
    }
}
