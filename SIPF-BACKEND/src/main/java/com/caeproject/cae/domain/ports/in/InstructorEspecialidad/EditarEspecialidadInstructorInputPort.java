package com.caeproject.cae.domain.ports.in.InstructorEspecialidad;

import com.caeproject.cae.application.usecases.EspecialidadInstructor.commands.EditarEspecialidadInstructorCommand;
import com.caeproject.cae.application.usecases.especialidad.commands.EditarEspecialidadCommand;
import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;

public interface EditarEspecialidadInstructorInputPort {
    InstructorEspecialidad editarInstructorEspecialidad (EditarEspecialidadInstructorCommand command, Long usuarioId);
}
