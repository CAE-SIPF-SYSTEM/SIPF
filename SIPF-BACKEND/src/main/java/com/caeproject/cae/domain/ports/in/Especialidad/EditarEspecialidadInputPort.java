package com.caeproject.cae.domain.ports.in.Especialidad;

import com.caeproject.cae.application.usecases.especialidad.commands.EditarEspecialidadCommand;
import com.caeproject.cae.domain.ports.model.Especialidad;

public interface EditarEspecialidadInputPort {
    Especialidad editarEspecialidad (EditarEspecialidadCommand command, Long id);
}
