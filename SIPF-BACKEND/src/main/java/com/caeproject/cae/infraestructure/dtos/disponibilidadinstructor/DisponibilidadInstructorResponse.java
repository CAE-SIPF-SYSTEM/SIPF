package com.caeproject.cae.infraestructure.dtos.disponibilidadinstructor;

import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;

import java.util.List;

public class DisponibilidadInstructorResponse {

    private Long usuarioId;
    private List<DiasDisponibles> diasDisponibles;
    private Long horasMaximas;
    private Long horasAsignadas;
    private Long horasDisponibles;
    private List<Municipio> municipios;
    private com.caeproject.cae.domain.ports.model.enums.Jornada jornada;

    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<DiasDisponibles> getDiasDisponibles() {
        return diasDisponibles;
    }

    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
    }

    public Long getHorasMaximas() {
        return horasMaximas;
    }

    public void setHorasMaximas(Long horasMaximas) {
        this.horasMaximas = horasMaximas;
    }

    public Long getHorasAsignadas() {
        return horasAsignadas;
    }

    public void setHorasAsignadas(Long horasAsignadas) {
        this.horasAsignadas = horasAsignadas;
    }

    public Long getHorasDisponibles() {
        return horasDisponibles;
    }

    public void setHorasDisponibles(Long horasDisponibles) {
        this.horasDisponibles = horasDisponibles;
    }

    public com.caeproject.cae.domain.ports.model.enums.Jornada getJornada() {
        return jornada;
    }

    public void setJornada(com.caeproject.cae.domain.ports.model.enums.Jornada jornada) {
        this.jornada = jornada;
    }
}
