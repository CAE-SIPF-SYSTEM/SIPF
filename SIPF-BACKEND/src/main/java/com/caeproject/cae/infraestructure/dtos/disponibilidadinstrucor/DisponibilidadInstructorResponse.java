package com.caeproject.cae.infraestructure.dtos.disponibilidadinstrucor;

import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;

import java.util.List;

public class DisponibilidadInstructorResponse {


    private Long usuarioId;
    private List<DiasDisponibles> diasDisponibles;
    private Long horasMaximas;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<DiasDisponibles> getDiasDisponibles() {return diasDisponibles;}
    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) {this.diasDisponibles = diasDisponibles;}


    public Long getHorasMaximas() {
        return horasMaximas;
    }

    public void setHorasMaximas(Long horasMaximas) {
        this.horasMaximas = horasMaximas;
    }

}
