package com.caeproject.cae.application.usecases.disponibilidadinstructor.commands;

import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;

import java.util.List;

public class EditarDisponibilidadCommand {

    private List<DiasDisponibles> diasDisponibles;
    private List<Municipio> municipios;


    public List<DiasDisponibles> getDiasDisponibles() {
        return diasDisponibles;
    }

    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
    }

    private com.caeproject.cae.domain.ports.model.enums.Jornada jornada;
    public com.caeproject.cae.domain.ports.model.enums.Jornada getJornada() {return jornada;}
    public void setJornada(com.caeproject.cae.domain.ports.model.enums.Jornada jornada) {this.jornada = jornada;}

    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }
}

