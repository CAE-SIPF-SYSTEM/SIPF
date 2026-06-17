package com.caeproject.cae.infraestructure.dtos.programa;

import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EditarProgramaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El municipio es obligatorio")
    private String municipio;

    @NotNull(message = "El nivel de formación es obligatorio")
    private NivelFormacion nivelFormacion;

    @NotNull(message = "La jornada es obligatoria")
    private Jornada jornada;

    @NotNull(message = "La duración en prácticas es obligatoria")
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
