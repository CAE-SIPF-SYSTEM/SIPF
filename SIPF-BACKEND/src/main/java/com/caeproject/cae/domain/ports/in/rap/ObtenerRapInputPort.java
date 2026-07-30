package com.caeproject.cae.domain.ports.in.Rap;

import com.caeproject.cae.domain.ports.model.Rap;

import java.util.List;

public interface ObtenerRapInputPort {
    Rap obtenerRap (Long id);
    List<Rap> obtenerRapCompetencia (Long competenciaId);
}
