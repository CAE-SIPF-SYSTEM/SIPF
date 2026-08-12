package com.caeproject.cae.domain.ports.service;

import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;

import java.util.List;

public class ValidarCruceHorarioInstructor {



    public boolean validarCrucehorario(
            DisponibilidadInstructor disponibilidadInstructor,
            Programa programa,
            List<ProgramacionAcademica> programacionAcademicaList,
            ProgramacionAcademica nuevaprogramacion
    ){
        if (programacionAcademicaList == null || programacionAcademicaList.isEmpty()) {
            return true;
        }
        for (ProgramacionAcademica pa : programacionAcademicaList) {
            if (pa.getUsuarioId() != null && pa.getUsuarioId().equals(nuevaprogramacion.getUsuarioId())) {
                if (pa.getTrimestreId() != null && pa.getTrimestreId().equals(nuevaprogramacion.getTrimestreId()) &&
                    pa.getRapId() != null && pa.getRapId().equals(nuevaprogramacion.getRapId())) {
                    return false;
                }
            }
        }
        return true;
    }


}
