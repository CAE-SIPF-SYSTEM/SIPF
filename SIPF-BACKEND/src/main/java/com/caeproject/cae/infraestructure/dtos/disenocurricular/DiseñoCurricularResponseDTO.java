package com.caeproject.cae.infraestructure.dtos.disenocurricular;

public class DiseñoCurricularResponseDTO {
    private Long programaId;
    private Integer numeroTrimestre;
    private Long rapId;
    private Integer horasPresenciales;

    public Long getProgramaId() {return programaId;}
    public void setProgramaId(Long programaId) {this.programaId = programaId;}

    public Integer getNumeroTrimestre() {return numeroTrimestre;}
    public void setNumeroTrimestre(Integer numeroTrimestre) {this.numeroTrimestre = numeroTrimestre;}

    public Long getRapId() {return rapId;}
    public void setRapId(Long rapId) {this.rapId = rapId;}

    public Integer getHorasPresenciales() {return horasPresenciales;}
    public void setHorasPresenciales(Integer horasPresenciales) {this.horasPresenciales = horasPresenciales;}
}
