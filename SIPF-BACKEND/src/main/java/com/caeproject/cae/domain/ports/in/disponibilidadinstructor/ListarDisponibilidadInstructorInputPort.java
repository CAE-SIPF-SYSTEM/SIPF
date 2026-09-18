package com.caeproject.cae.domain.ports.in.disponibilidadinstructor;

import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;

import java.util.List;

public interface ListarDisponibilidadInstructorInputPort {
    List<DisponibilidadInstructor> listarDisponibilidad();
}
