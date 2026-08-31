package com.caeproject.cae.domain.ports.in.ficha;

import com.caeproject.cae.infraestructure.dtos.ficha.FichaAvanceResponse;

import java.util.List;

public interface ObtenerAvanceFichasInputPort {
    List<FichaAvanceResponse> obtenerAvanceFichas();
}
