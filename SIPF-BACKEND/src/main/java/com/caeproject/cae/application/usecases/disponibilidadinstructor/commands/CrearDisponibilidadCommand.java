package com.caeproject.cae.application.usecases.disponibilidadinstructor.commands;

import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;
import com.caeproject.cae.domain.ports.model.enums.Jornada;

import java.util.List;

public class CrearDisponibilidadCommand {
    private Long usuarioId;
    private List<DiasDisponibles> diasDisponibles;
    private List<Municipio> municipios;
    private Jornada jornada;

    public Long getUsuarioId() {return this.usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}

    public List<DiasDisponibles> getDiasDisponibles() {
        return diasDisponibles;
    }

    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
    }

    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }

    public com.caeproject.cae.domain.ports.model.enums.Jornada getJornada() {return jornada;}
    public void setJornada(com.caeproject.cae.domain.ports.model.enums.Jornada jornada) {this.jornada = jornada;}
}
