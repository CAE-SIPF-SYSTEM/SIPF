package com.caeproject.cae.domain.ports.in.especialidad;

import com.caeproject.cae.domain.ports.model.Especialidad;

import java.util.List;

public interface ListarEspecialidadInputPort {
    List<Especialidad> listarEspecialidades();
}
