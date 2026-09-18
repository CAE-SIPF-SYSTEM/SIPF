package com.caeproject.cae.application.usecases.competencia.commands;

import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;

public class CrearCompetenciaCommand {

    private String nombre;
    private TipoCompetencia tipoCompetencia;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoCompetencia getTipoCompetencia() { return tipoCompetencia; }
    public void setTipoCompetencia(TipoCompetencia tipoCompetencia) { this.tipoCompetencia = tipoCompetencia; }
}
