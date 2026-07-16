package com.caeproject.cae.domain.ports.model;

public class CompetenciaEspecialidad {
    private Long competenciaId;
    private Long especialidadId;

    public CompetenciaEspecialidad() {
    }

    public CompetenciaEspecialidad(Long competenciaId, Long especialidadId) {
        this.competenciaId = competenciaId;
        this.especialidadId = especialidadId;
    }

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
