package com.caeproject.cae.infraestructure.dtos.competencia;

import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EditarCompetenciaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El tipo de competencia es obligatorio")
    private TipoCompetencia tipoCompetencia;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoCompetencia getTipoCompetencia() { return tipoCompetencia; }
    public void setTipoCompetencia(TipoCompetencia tipoCompetencia) { this.tipoCompetencia = tipoCompetencia; }
}
