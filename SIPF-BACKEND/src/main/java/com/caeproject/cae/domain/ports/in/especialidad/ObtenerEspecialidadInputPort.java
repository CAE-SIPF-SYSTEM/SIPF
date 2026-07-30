package com.caeproject.cae.domain.ports.in.Especialidad;

import com.caeproject.cae.domain.ports.model.Especialidad;

public interface ObtenerEspecialidadInputPort {
    Especialidad obtenerEspecialidad (Long id);
}
