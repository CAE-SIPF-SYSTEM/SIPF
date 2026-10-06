package com.caeproject.cae.domain.ports.in.asignarinstructor;

import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.PerfilBase;
import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;

import java.util.List;

public interface SugerirInstructorInputPort {
    List<DisponibilidadInstructor> sugerirInstructores(Long competenciaId, Long fichaId, Long horasRequeridas);
}
