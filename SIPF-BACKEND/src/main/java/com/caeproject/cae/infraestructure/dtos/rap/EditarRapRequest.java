package com.caeproject.cae.infraestructure.dtos.rap;

import jakarta.validation.constraints.Min;

public class EditarRapRequest {

    private Long competenciaId;

    private String descripcion;

    @Min(value = 1, message = "Debe haber al menos 1 hora presencial")
    private Integer horasPresenciales;

    private Boolean estado;
    public Long getCompetenciaId() { return competenciaId; }
    public void setCompetenciaId(Long competenciaId) { this.competenciaId = competenciaId; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getHorasPresenciales() { return horasPresenciales; }
    public void setHorasPresenciales(Integer horasPresenciales) { this.horasPresenciales = horasPresenciales; }
    public Boolean getEstado() {return estado;}
    public void setEstado(Boolean estado) {this.estado = estado;}
}

