package com.caeproject.cae.domain.ports.in.especialidad;

import com.caeproject.cae.application.usecases.especialidad.commands.CrearEspecialidadCommand;
import com.caeproject.cae.domain.ports.model.Especialidad;

public interface CrearEspecialidadInputPort {
    Especialidad crearEspecialidad (CrearEspecialidadCommand command);
}
