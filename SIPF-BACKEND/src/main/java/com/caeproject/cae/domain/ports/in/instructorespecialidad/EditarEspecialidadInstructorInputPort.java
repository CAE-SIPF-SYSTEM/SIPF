package com.caeproject.cae.domain.ports.in.instructorespecialidad;

import com.caeproject.cae.application.usecases.especialidadinstructor.commands.EditarEspecialidadInstructorCommand;
import com.caeproject.cae.application.usecases.especialidad.commands.EditarEspecialidadCommand;
import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;

public interface EditarEspecialidadInstructorInputPort {
    InstructorEspecialidad editarInstructorEspecialidad (EditarEspecialidadInstructorCommand command, Long usuarioId);
}
