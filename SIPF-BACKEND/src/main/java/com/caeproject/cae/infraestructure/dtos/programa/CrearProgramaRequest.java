package com.caeproject.cae.infraestructure.dtos.programa;

import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class CrearProgramaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El municipio es obligatorio")
    private Municipio municipio;

    @NotNull(message = "El nivel de formación es obligatorio")
    private NivelFormacion nivelFormacion;

    @NotNull(message = "La jornada es obligatoria")
    private Jornada jornada;

    @NotNull(message = "La duración en prácticas es obligatoria")
    @Min(value = 1, message = "La duración mínima es 1")
    private Integer duracionpracticas;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public NivelFormacion getNivelFormacion() { return nivelFormacion; }
    public void setNivelFormacion(NivelFormacion nivelFormacion) { this.nivelFormacion = nivelFormacion; }
    public Jornada getJornada() { return jornada; }
    public void setJornada(Jornada jornada) { this.jornada = jornada; }
    public Integer getDuracionpracticas() { return duracionpracticas; }
    public void setDuracionpracticas(Integer duracionpracticas) { this.duracionpracticas = duracionpracticas; }
}
