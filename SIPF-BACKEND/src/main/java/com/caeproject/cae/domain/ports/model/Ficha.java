package com.caeproject.cae.domain.ports.model;

import java.util.Date;

public class Ficha {
    private Long id;
    private String codigoFicha;
    private Long programaId;
    private Date fechaInicio;
    private Date fechaFin;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getCodigoFicha() {return codigoFicha;}
    public void setCodigoFicha(String codigoFicha) {this.codigoFicha = codigoFicha;}

    public Long getProgramaId() {return programaId;}
    public void setProgramaId(Long programaId) {this.programaId = programaId;}

    public Date getFechaInicio() {return fechaInicio;}
    public void setFechaInicio(Date fechaInicio) {this.fechaInicio = fechaInicio;}

    public Date getFechaFin() {return fechaFin;}
    public void setFechaFin(Date fechaFin) {this.fechaFin = fechaFin;}
}
