package com.caeproject.cae.domain.ports.exceptions.diseñocurricularexception;

public class PlantillaVaciaException extends RuntimeException {
    public PlantillaVaciaException(Long programaId, Integer numeroTrimestre) {
        super("No se encontraron RAPs en el Diseño Curricular para el Programa " + programaId + " en el Trimestre " + numeroTrimestre + ". Por favor, suba el archivo Excel de programación primero.");
    }
}
