package com.caeproject.cae.infraestructure.dtos.competencia;

import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;

public class CompetenciaResponse {

    private Long id;
    private String nombre;
    private TipoCompetencia tipoCompetencia;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoCompetencia getTipoCompetencia() { return tipoCompetencia; }
    public void setTipoCompetencia(TipoCompetencia tipoCompetencia) { this.tipoCompetencia = tipoCompetencia; }
}
