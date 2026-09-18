package com.caeproject.cae.domain.ports.model;

public class ProgramacionAcademica {
    private Long id;
    private Long trimestreId;
    private Long rapId;
    private Long usuarioId;
    private Long programaId;
    private Long fichaId;


    public Long getProgramaId() {
        return programaId;
    }

    public Long getFichaId() {
        return fichaId;
    }

    public void setFichaId(Long fichaId) {
        this.fichaId = fichaId;
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
