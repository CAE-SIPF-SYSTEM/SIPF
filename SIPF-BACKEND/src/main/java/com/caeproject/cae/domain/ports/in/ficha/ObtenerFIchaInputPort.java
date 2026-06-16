package com.caeproject.cae.domain.ports.in.ficha;

import com.caeproject.cae.domain.ports.model.ficha.Ficha;

public interface ObtenerFIchaInputPort {
    Ficha obtenerFicha (Long id);
}
