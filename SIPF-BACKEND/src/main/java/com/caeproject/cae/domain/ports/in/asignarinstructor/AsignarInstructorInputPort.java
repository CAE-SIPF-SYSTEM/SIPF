package com.caeproject.cae.domain.ports.in.asignarinstructor;

import com.caeproject.cae.application.usecases.asignacioninstructor.commands.AsignarInstructorCommand;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;

public interface AsignarInstructorInputPort {
    Long asignarInstructor (AsignarInstructorCommand command);
}
