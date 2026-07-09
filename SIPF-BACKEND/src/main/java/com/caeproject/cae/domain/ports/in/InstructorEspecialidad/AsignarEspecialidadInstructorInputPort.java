package com.caeproject.cae.domain.ports.in.InstructorEspecialidad;

import com.caeproject.cae.application.usecases.EspecialidadInstructor.commands.AsignarEspecialidadInstructorCommand;
import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;

public interface AsignarEspecialidadInstructorInputPort {
    InstructorEspecialidad asignarEspecialidadInstructor(AsignarEspecialidadInstructorCommand command);
}
