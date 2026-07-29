package com.caeproject.cae.infraestructure.adapter.out.programacionAcademica;


import jakarta.persistence.*;

@Entity
@Table(name = "ProgramacionAcademica")
public class ProgramacionAcademicaEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "trimestreId", nullable = false)
    private Long trimestreId;

    @Column(name = "rapId", nullable = false)
    private Long rapId;

    @Column(name = "usuarioId", nullable = false)
    private Long usuarioId;

    @Column(name = "programaId", nullable = false)
    private Long programaId;


    public Long getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Long programaId) {
        this.programaId = programaId;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getTrimestreId() {return trimestreId;}
    public void setTrimestreId(Long trimestreId) {this.trimestreId = trimestreId;}

    public Long getRapId() {return rapId;}
    public void setRapId(Long rapId) {this.rapId = rapId;}

    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}
}
