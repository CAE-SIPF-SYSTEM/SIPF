package com.caeproject.cae.infraestructure.dtos.asignacioninstructor;

import java.util.List;

public class ResumenFichaCompetenciasResponse {
    private Long fichaId;
    private String codigoFicha;
    private List<ResumenCompetenciaResponse> competencias;

    public Long getFichaId() {
        return fichaId;
    }

    public void setFichaId(Long fichaId) {
        this.fichaId = fichaId;
    }

    public String getCodigoFicha() {
        return codigoFicha;
    }

    public void setCodigoFicha(String codigoFicha) {
        this.codigoFicha = codigoFicha;
    }

    public List<ResumenCompetenciaResponse> getCompetencias() {
        return competencias;
    }

    public void setCompetencias(List<ResumenCompetenciaResponse> competencias) {
        this.competencias = competencias;
    }
}
