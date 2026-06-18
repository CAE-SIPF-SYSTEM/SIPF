package com.caeproject.cae.domain.ports.in.competencia;

import com.caeproject.cae.domain.ports.model.competencia.Competencia;

import java.util.List;

public interface ListarCompetenciasInputPort {
    List<Competencia> listarCompetencia();
}
