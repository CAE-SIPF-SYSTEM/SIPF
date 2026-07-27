package com.caeproject.cae.application.usecases.ProgramacionAcademica.commands;

public class CrearProgramacionCommand {


    private Long trimestreId;
    private Long rapId;
    private Long usuarioId;
    private Long programaId;

    public Long getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Long programaId) {
        this.programaId = programaId;
    }

    public Long getTrimestreId() {
        return trimestreId;
    }

    public void setTrimestreId(Long trimestreId) {
        this.trimestreId = trimestreId;
    }

    public Long getRapId() {return rapId;}
    public void setRapId(Long rapId) {this.rapId = rapId;}

    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}


}
