package com.caeproject.cae.domain.ports.model;

import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;

public class Programa {

    private Long id;
    private String nombre;
    private Municipio municipio;

    private NivelFormacion nivelFormacion;
    private Jornada jornada;

    private Integer duracionpracticas;


    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public NivelFormacion getNivelFormacion() {return nivelFormacion;}

    public void setNivelFormacion(NivelFormacion nivelFormacion) {this.nivelFormacion = nivelFormacion;}

    public Jornada getJornada() {return jornada;}

    public void setJornada(Jornada jornada) {this.jornada = jornada;}

    public Integer getDuracionpracticas() {return duracionpracticas;}

    public void setDuracionpracticas(Integer duracionpracticas) {this.duracionpracticas = duracionpracticas;}
}
