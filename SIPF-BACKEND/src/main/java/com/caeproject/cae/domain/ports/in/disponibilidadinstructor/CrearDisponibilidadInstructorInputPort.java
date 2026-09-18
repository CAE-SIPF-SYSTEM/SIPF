package com.caeproject.cae.domain.ports.in.disponibilidadinstructor;

import com.caeproject.cae.application.usecases.disponibilidadinstructor.commands.CrearDisponibilidadCommand;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;

public interface CrearDisponibilidadInstructorInputPort {
    DisponibilidadInstructor crearDisponibilidad (CrearDisponibilidadCommand command);
}
