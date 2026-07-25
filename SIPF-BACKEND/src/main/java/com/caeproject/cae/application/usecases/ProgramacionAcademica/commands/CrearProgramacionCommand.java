package com.caeproject.cae.application.usecases.ProgramacionAcademica.commands;

public class CrearProgramacionCommand {


    private Long trimstreId;
    private Long rapId;
    private Long usuarioId;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getTrimstreId() {return trimstreId;}
    public void setTrimstreId(Long trimstreId) {this.trimstreId = trimstreId;}

    public Long getRapId() {return rapId;}
    public void setRapId(Long rapId) {this.rapId = rapId;}

    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}

}
