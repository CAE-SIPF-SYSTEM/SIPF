package com.caeproject.cae.application.usecases.programa.commands;

import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;

public class CrearProgramaCommand {

    private String nombre;
    private String municipio;
    private NivelFormacion nivelFormacion;
    private Jornada jornada;
    private Integer duracionpracticas;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }
    public NivelFormacion getNivelFormacion() { return nivelFormacion; }
    public void setNivelFormacion(NivelFormacion nivelFormacion) { this.nivelFormacion = nivelFormacion; }
    public Jornada getJornada() { return jornada; }
    public void setJornada(Jornada jornada) { this.jornada = jornada; }
    public Integer getDuracionpracticas() { return duracionpracticas; }
    public void setDuracionpracticas(Integer duracionpracticas) { this.duracionpracticas = duracionpracticas; }
}
