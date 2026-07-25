package com.caeproject.cae.domain.ports.model;

import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;
import com.caeproject.cae.domain.ports.model.enums.Jornada;

import java.util.List;

public class DisponibilidadInstructor {


    private Long usuarioId;
    private List<DiasDisponibles> diasDisponibles;
    private Long horasMaximas;
    private Jornada jornada;
    private List<Municipio> municipios;
    private Long horasAsignadas;



    //metodo de calculo horas asignadas
    public Long getHorasDisponibles(){
        if (horasAsignadas == null){
            throw new IllegalStateException("Las horas asignadas no ha sido calculadas aun");
        }
        return horasMaximas - horasAsignadas;
    }


    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}

    public List<DiasDisponibles> getDiasDisponibles() {
        return diasDisponibles;
    }

    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
    }

    public Long getHorasMaximas() {return horasMaximas;}
    public void setHorasMaximas(Long horasMaximas) {this.horasMaximas = horasMaximas;}

    public Jornada getJornada() {return jornada;}
    public void setJornada(Jornada jornada) {this.jornada = jornada;}

    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }

    public Long getHorasAsignadas() { return horasAsignadas; }
    public void setHorasAsignadas(Long horasAsignadas) { this.horasAsignadas = horasAsignadas; }
}
