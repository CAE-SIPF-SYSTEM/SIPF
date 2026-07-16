package com.caeproject.cae.infraestructure.dtos.especialidad;

public class EspecialidadResponseDTO {
    private Long id;
    private String nombreEspecialidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }

    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }
}
