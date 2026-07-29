package com.caeproject.cae.domain.ports.in.asignarinstructor;

import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;

import java.util.List;

public interface SugerirInstructorInputPort {
    List<DisponibilidadInstructor> sugerirInstructores(Long competenciaId, Long programaId, Long horasRequeridas);
}
