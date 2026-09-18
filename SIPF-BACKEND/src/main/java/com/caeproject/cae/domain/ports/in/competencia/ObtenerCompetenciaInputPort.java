package com.caeproject.cae.domain.ports.in.competencia;

import com.caeproject.cae.domain.ports.model.Competencia;
import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;

import java.util.List;

public interface ObtenerCompetenciaInputPort {
    Competencia obtenerCompetencia(Long id);
    List<Competencia> obtenerPorTipoCompetencia(TipoCompetencia tipoCompetencia);
}
