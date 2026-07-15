package com.caeproject.cae.infraestructure.dtos.disponibilidadinstrucor;

import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;

import java.util.List;

public class EditarDisponibilidadInstructorRequest {

    private List<DiasDisponibles> diasDisponibles;

    public List<DiasDisponibles> getDiasDisponibles() {return diasDisponibles;}
    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) { this.diasDisponibles = diasDisponibles; }

}
