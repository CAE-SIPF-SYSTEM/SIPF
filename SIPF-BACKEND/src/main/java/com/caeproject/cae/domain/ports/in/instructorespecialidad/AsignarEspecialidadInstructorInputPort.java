package com.caeproject.cae.domain.ports.in.instructorespecialidad;

import com.caeproject.cae.application.usecases.especialidadinstructor.commands.AsignarEspecialidadInstructorCommand;
import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;

public interface AsignarEspecialidadInstructorInputPort {
    InstructorEspecialidad asignarEspecialidadInstructor(AsignarEspecialidadInstructorCommand command);
}
