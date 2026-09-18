package com.caeproject.cae.domain.ports.in.disponibilidadinstructor;

import com.caeproject.cae.application.usecases.disponibilidadinstructor.commands.EditarDisponibilidadCommand;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;

public interface EditarDisponibilidadInstructorInputPort {
    DisponibilidadInstructor editarDisponibilidad (EditarDisponibilidadCommand command, Long usuarioId);
}
