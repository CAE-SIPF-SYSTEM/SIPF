package com.caeproject.cae.application.usecases.CompetenciaEspecialidad.commands;

public class AsignarEspecialidadCompetenciaCommand {
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
