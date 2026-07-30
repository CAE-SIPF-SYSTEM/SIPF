package com.caeproject.cae.domain.ports.in.DisponibilidadInstructor;

import com.caeproject.cae.application.usecases.disponibilidadInstructor.commands.EditarDisponibilidadCommand;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;

public interface EditarDisponibilidadInstructorInputPort {
    DisponibilidadInstructor editarDisponibilidad (EditarDisponibilidadCommand command, Long usuarioId);
}
