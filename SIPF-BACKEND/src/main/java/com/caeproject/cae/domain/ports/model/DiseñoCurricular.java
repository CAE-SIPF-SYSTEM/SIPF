package com.caeproject.cae.domain.ports.model;

public class DiseñoCurricular {

    private Long id;
    private Long programaId;
    private Integer numeroTrimestre;
    private Long rapId;


    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getProgramaId() {return programaId;}
    public void setProgramaId(Long programaId) {this.programaId = programaId;}

    public Integer getNumeroTrimestre() {return numeroTrimestre;}
    public void setNumeroTrimestre(Integer numeroTrimestre) {this.numeroTrimestre = numeroTrimestre;}

    public Long getRapId() {return rapId;}
    public void setRapId(Long rapId) {this.rapId = rapId;}
}
