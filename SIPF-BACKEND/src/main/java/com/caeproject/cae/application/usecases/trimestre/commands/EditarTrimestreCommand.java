package com.caeproject.cae.application.usecases.trimestre.commands;

import java.util.Date;

public class EditarTrimestreCommand {
    private Long fichaId;
    private Integer numeroTrimestre;
    private Date fechaInicio;
    private Date fechaFin;

    public Long getFichaId() {
        return fichaId;
    }

    public void setFichaId(Long fichaId) {
        this.fichaId = fichaId;
    }

    public Integer getNumeroTrimestre() {
        return numeroTrimestre;
    }

    public void setNumeroTrimestre(Integer numeroTrimestre) {
        this.numeroTrimestre = numeroTrimestre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
