package com.caeproject.cae.domain.ports.in.ficha;

import com.caeproject.cae.domain.ports.model.ficha.Ficha;

public interface RegistrarFichaInputPort {
    Ficha registrarFicha(Ficha ficha);
}
