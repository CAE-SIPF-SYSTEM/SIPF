package com.caeproject.cae.application.usecases.rap.commands;

public class CrearRapCommand {

    private Long competenciaId;
    private String descripcion;
    private Integer horasPresenciales;

    public Long getCompetenciaId() { return competenciaId; }
    public void setCompetenciaId(Long competenciaId) { this.competenciaId = competenciaId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getHorasPresenciales() { return horasPresenciales; }
    public void setHorasPresenciales(Integer horasPresenciales) { this.horasPresenciales = horasPresenciales; }
}
