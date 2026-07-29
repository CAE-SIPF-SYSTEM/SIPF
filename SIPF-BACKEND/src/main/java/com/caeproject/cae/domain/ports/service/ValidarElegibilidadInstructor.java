package com.caeproject.cae.domain.ports.service;

import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;
import com.caeproject.cae.domain.ports.model.Programa;

public class ValidarElegibilidadInstructor {
    public boolean esElegible(
            DisponibilidadInstructor disponibilidadInstructor,
            InstructorEspecialidad instructorEspecialidad,
            CompetenciaEspecialidad competenciaEspecialidad,
            Programa programa,
            Long horasRequeridas
    ){

        boolean especialidadValida = instructorEspecialidad.getEspecialidadId().equals(competenciaEspecialidad.getEspecialidadId());

        boolean municipioValido = disponibilidadInstructor.getMunicipios().stream()
                .anyMatch(m -> m.getId().equals(programa.getMunicipio().getId()));

        boolean horasSuficientes = disponibilidadInstructor.getHorasDisponibles() >= horasRequeridas;

        boolean tieneDias = disponibilidadInstructor.getDiasDisponibles() != null && !disponibilidadInstructor.getDiasDisponibles().isEmpty();

        return especialidadValida && municipioValido && horasSuficientes && tieneDias;

    }
}
