package com.caeproject.cae.domain.ports.in.ficha;

import com.caeproject.cae.domain.ports.model.Ficha;

import java.util.List;

public interface ObtenerFIchaInputPort {
    Ficha obtenerFicha (Long id);
    List<Ficha> obtenerFichaPorProgramaId(Long programaId);
}
