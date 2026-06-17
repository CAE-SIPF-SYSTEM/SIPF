package com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException;

import java.time.LocalDate;

public class FichaFueraDeVigenciaException extends RuntimeException {
    public FichaFueraDeVigenciaException(String codigoFicha, LocalDate fechaIntento, LocalDate finLectiva) {
        super(String.format("No se puede programar en la ficha %s. La fecha intentada (%s) supera la fecha de fin de la etapa lectiva (%s).",
                codigoFicha, fechaIntento, finLectiva));
    }
}
