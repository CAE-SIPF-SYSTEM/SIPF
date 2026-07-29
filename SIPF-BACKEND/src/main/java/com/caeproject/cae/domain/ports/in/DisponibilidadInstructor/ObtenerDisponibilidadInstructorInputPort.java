package com.caeproject.cae.domain.ports.in.DisponibilidadInstructor;

import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;

import java.util.List;

public interface ObtenerDisponibilidadInstructorInputPort {
    DisponibilidadInstructor obtenerDisponiblidad(Long usuarioId);
    List<DisponibilidadInstructor> obtenerPorDiasDisponibles (DiasDisponibles diasDisponibles);
}
