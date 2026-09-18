package com.caeproject.cae.infraestructure.dtos.competenciaespecialidad;

public class CompetenciaEspecialidadRequestDTO {
    private Long competenciaId;
    private Long especialidadId;

    public Long getCompetenciaId() {
        return competenciaId;
    }

    public void setCompetenciaId(Long competenciaId) {
        this.competenciaId = competenciaId;
    }

    public Long getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(Long especialidadId) {
        this.especialidadId = especialidadId;
    }
}
