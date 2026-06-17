package com.caeproject.cae.domain.ports.in.ficha;

import com.caeproject.cae.domain.ports.model.ficha.Ficha;

import java.util.List;

public interface ListarFichasInputPort {
    List<Ficha> listarFichas();
}
