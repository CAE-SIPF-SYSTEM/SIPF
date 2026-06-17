package com.caeproject.cae.domain.ports.in.programa;

import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import com.caeproject.cae.domain.ports.model.programa.Programa;

import java.util.List;
import java.util.Optional;

public interface ObtenerProgramaInputPort {
    Programa obtenerPrograma(Long id);
    List<Programa> obtenerProgramaJornada (Jornada jornada);
    List<Programa> obtenerProgramaNivelFormacion(NivelFormacion nivelFormacion);
}
