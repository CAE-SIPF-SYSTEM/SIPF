package com.caeproject.cae.infraestructure.dtos.disponibilidadinstrucor;

import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;

import java.util.List;

public class EditarDisponibilidadInstructorRequest {

    private List<DiasDisponibles> diasDisponibles;
    private List<String> municipios;

    public List<String> getMunicipios() { return municipios; }
    public void setMunicipios(List<String> municipios) { this.municipios = municipios; }

    public List<DiasDisponibles> getDiasDisponibles() {return diasDisponibles;}
    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) { this.diasDisponibles = diasDisponibles; }

    private com.caeproject.cae.domain.ports.model.enums.Jornada jornada;
    public com.caeproject.cae.domain.ports.model.enums.Jornada getJornada() {return jornada;}
    public void setJornada(com.caeproject.cae.domain.ports.model.enums.Jornada jornada) {this.jornada = jornada;}
}
