package com.caeproject.cae.application.usecases.ficha.commands;

import java.util.Date;

public class EditarFichaCommand {

    private String codigoFicha;
    private Long programaId;
    private Date fechaInicio;
    private Date fechaFin;

    public String getCodigoFicha() { return codigoFicha; }
    public void setCodigoFicha(String codigoFicha) { this.codigoFicha = codigoFicha; }

    public Long getProgramaId() { return programaId; }
    public void setProgramaId(Long programaId) { this.programaId = programaId; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
}
