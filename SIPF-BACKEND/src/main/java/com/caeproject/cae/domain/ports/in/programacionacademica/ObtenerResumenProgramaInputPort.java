package com.caeproject.cae.domain.ports.in.programacionacademica;

import com.caeproject.cae.infraestructure.dtos.asignacioninstructor.ResumenProgramaCompetenciasResponse;

public interface ObtenerResumenProgramaInputPort {
    ResumenProgramaCompetenciasResponse obtenerResumenPrograma(Long programaId);
}
