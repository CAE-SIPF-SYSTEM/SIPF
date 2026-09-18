package com.caeproject.cae.infraestructure.dtos.asignacioninstructor;

import java.util.List;

public class ResumenProgramaCompetenciasResponse {

    private Long programaId;
    private String nombrePrograma;
    private List<ResumenCompetenciaResponse> competencias;

    public Long getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Long programaId) {
        this.programaId = programaId;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public List<ResumenCompetenciaResponse> getCompetencias() {
        return competencias;
    }

    public void setCompetencias(List<ResumenCompetenciaResponse> competencias) {
        this.competencias = competencias;
    }
}
