package com.caeproject.cae.domain.ports.in.programacionacademica;

import com.caeproject.cae.infraestructure.dtos.asignacioninstructor.ResumenFichaCompetenciasResponse;

public interface ObtenerResumenFichaInputPort {
    ResumenFichaCompetenciasResponse obtenerResumenFicha(Long fichaId);
}
