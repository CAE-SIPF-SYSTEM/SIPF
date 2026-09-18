package com.caeproject.cae.domain.ports.in.trimestre;

import com.caeproject.cae.domain.ports.model.Trimestre;

import java.util.List;

public interface ObtenerTrimestreInputPort {
    Trimestre obtenerTrimestre(Long id);
    List<Trimestre> obtenerTrimestreFicha(Long fichaId);
}
