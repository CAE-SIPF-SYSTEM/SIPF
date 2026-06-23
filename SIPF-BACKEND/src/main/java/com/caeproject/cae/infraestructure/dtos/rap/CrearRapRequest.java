package com.caeproject.cae.infraestructure.dtos.rap;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class CrearRapRequest {

    @NotNull(message = "El ID de la competencia es obligatorio")
    private Long competenciaId;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "Las horas presenciales son obligatorias")
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
