package com.caeproject.cae.domain.ports.model;

import java.util.Date;

public class Trimestre {
    private Long id;
    private Long fichaId;
    private Integer anio;
    private Integer numeroTrimestre;
    private Date fechaInicio;
    private Date fechaFin;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getFichaId() {return fichaId;}
    public void setFichaId(Long fichaId) {this.fichaId = fichaId;}

    public Integer getAnio() {return anio;}
    public void setAnio(Integer anio) {this.anio = anio;}

    public Integer getNumeroTrimestre() {return numeroTrimestre;}
    public void setNumeroTrimestre(Integer numeroTrimestre) {this.numeroTrimestre = numeroTrimestre;}

    public Date getFechaInicio() {return fechaInicio;}
    public void setFechaInicio(Date fechaInicio) {this.fechaInicio = fechaInicio;}

    public Date getFechaFin() {return fechaFin;}
    public void setFechaFin(Date fechaFin) {this.fechaFin = fechaFin;}
}
