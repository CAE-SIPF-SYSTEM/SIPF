package com.caeproject.cae.application.usecases.disponibilidadInstructor.commands;

import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;

import java.util.List;

public class EditarDisponibilidadCommand {

    private List<DiasDisponibles> diasDisponibles;
    private Long horasMaximas;
    private List<String> municipios;

    public Long getHorasMaximas() { return horasMaximas; }
    public void setHorasMaximas(Long horasMaximas) { this.horasMaximas = horasMaximas; }

    public List<DiasDisponibles> getDiasDisponibles() {
        return diasDisponibles;
    }

    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
    }

    private com.caeproject.cae.domain.ports.model.enums.Jornada jornada;
    public com.caeproject.cae.domain.ports.model.enums.Jornada getJornada() {return jornada;}
    public void setJornada(com.caeproject.cae.domain.ports.model.enums.Jornada jornada) {this.jornada = jornada;}

    public List<String> getMunicipios() {return municipios;}
    public void setMunicipios(List<String> municipios) {this.municipios = municipios;}

}

