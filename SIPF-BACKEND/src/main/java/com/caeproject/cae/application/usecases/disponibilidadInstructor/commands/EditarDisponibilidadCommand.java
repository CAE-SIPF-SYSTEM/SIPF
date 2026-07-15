package com.caeproject.cae.application.usecases.disponibilidadInstructor.commands;

import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;

import java.util.List;

public class EditarDisponibilidadCommand {

    private List<DiasDisponibles> diasDisponibles;
    private Long horasMaximas;
    public Long getHorasMaximas() { return horasMaximas; }
    public void setHorasMaximas(Long horasMaximas) { this.horasMaximas = horasMaximas; }

    public List<DiasDisponibles> getDiasDisponibles() {
        return diasDisponibles;
    }

    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
    }
   }

