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
    ) {
        return esElegible(disponibilidadInstructor, instructorEspecialidad, competenciaEspecialidad, programa, horasRequeridas, true);
    }

    public boolean esElegible(
            DisponibilidadInstructor disponibilidadInstructor,
            InstructorEspecialidad instructorEspecialidad,
            CompetenciaEspecialidad competenciaEspecialidad,
            Programa programa,
            Long horasRequeridas,
            boolean requiereEspecialidadEstricta
    ) {
        boolean especialidadValida;
        if (!requiereEspecialidadEstricta || competenciaEspecialidad == null) {
            especialidadValida = true;
        } else {
            especialidadValida = instructorEspecialidad != null
                    && instructorEspecialidad.getEspecialidadId() != null
                    && instructorEspecialidad.getEspecialidadId().equals(competenciaEspecialidad.getEspecialidadId());
        }

        boolean jornadaValida = programa.getJornada() == null 
                || disponibilidadInstructor.getJornada() == null
                || disponibilidadInstructor.getJornada().equals(programa.getJornada());

        boolean municipioValido = programa.getMunicipio() == null 
                || disponibilidadInstructor.getMunicipios() == null
                || disponibilidadInstructor.getMunicipios().isEmpty()
                || disponibilidadInstructor.getMunicipios().stream()
                        .anyMatch(m -> m.getId() != null && m.getId().equals(programa.getMunicipio().getId()));

        boolean horasSuficientes = disponibilidadInstructor.getHorasDisponibles() != null 
                && disponibilidadInstructor.getHorasDisponibles() >= horasRequeridas;

        boolean tieneDias = true;

        return especialidadValida && jornadaValida && municipioValido && horasSuficientes && tieneDias;
    }
}
