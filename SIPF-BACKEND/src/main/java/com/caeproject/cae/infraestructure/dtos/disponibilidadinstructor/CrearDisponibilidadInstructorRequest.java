package com.caeproject.cae.infraestructure.dtos.disponibilidadinstructor;

import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;

import java.util.List;

public class CrearDisponibilidadInstructorRequest {

    private Long usuarioId;
    private List<DiasDisponibles> diasDisponibles;
    private List<Municipio> municipios;

    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }

    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}

    public List<DiasDisponibles> getDiasDisponibles() {
        return diasDisponibles;
    }

    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
    }


    private com.caeproject.cae.domain.ports.model.enums.Jornada jornada;
    public com.caeproject.cae.domain.ports.model.enums.Jornada getJornada() {return jornada;}
    public void setJornada(com.caeproject.cae.domain.ports.model.enums.Jornada jornada) {this.jornada = jornada;}


}
