package com.caeproject.cae.domain.ports.exceptions;

public class CruceHorarioException extends RuntimeException {
    public CruceHorarioException(Long usuarioId, Long trimestreId, String diasDisponibles) {
        super(String.format("El instructor con ID %d no puede ser asignado debido a que ya se encuentra asignado en el Trimestre ID %d en los siguientes días: %s.",
                usuarioId, trimestreId, diasDisponibles));
    }
}
