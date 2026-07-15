package com.caeproject.cae.domain.ports.in.DisponibilidadInstructor;

import com.caeproject.cae.application.usecases.disponibilidadInstructor.commands.CrearDisponibilidadCommand;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;

public interface CrearDisponibilidadInstructorInputPort {
    DisponibilidadInstructor crearDisponibilidad (CrearDisponibilidadCommand command);
}
