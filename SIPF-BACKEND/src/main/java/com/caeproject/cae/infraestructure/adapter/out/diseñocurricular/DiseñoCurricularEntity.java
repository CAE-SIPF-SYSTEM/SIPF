package com.caeproject.cae.infraestructure.adapter.out.diseñocurricular;


import jakarta.persistence.*;

@Entity
@Table
public class DiseñoCurricularEntity {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "ProgramaID", nullable = false)
    private Long programaId;

    @Column (name = "NumeroTrimestre", nullable = false)
    private Integer numeroTrimestre;

    @Column (name = "rapId",nullable = false)
    private Long rapId;

    @Column (name = "horaspresenciales", nullable = false)
    private  Integer horaspresenciales;


    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getProgramaId() {return programaId;}
    public void setProgramaId(Long programaId) {this.programaId = programaId;}

    public Integer getNumeroTrimestre() {return numeroTrimestre;}
    public void setNumeroTrimestre(Integer numeroTrimestre) {this.numeroTrimestre = numeroTrimestre;}

    public Long getRapId() {return rapId;}
    public void setRapId(Long rapId) {this.rapId = rapId;}

    public Integer getHoraspresenciales() {
        return horaspresenciales;
    }

    public void setHoraspresenciales(Integer horaspresenciales) {
        this.horaspresenciales = horaspresenciales;
    }
}
