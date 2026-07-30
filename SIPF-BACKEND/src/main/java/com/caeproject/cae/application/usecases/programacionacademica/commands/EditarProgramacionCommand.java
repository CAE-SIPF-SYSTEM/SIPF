package com.caeproject.cae.application.usecases.ProgramacionAcademica.commands;

public class EditarProgramacionCommand {
    private Long trimstreId;
    private Long rapId;
    private Long usuarioId;
    private Integer horasPresenciales;

    public Long getTrimstreId() {return trimstreId;}
    public void setTrimstreId(Long trimstreId) {this.trimstreId = trimstreId;}

    public Long getRapId() {return rapId;}
    public void setRapId(Long rapId) {this.rapId = rapId;}

    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}

    public Integer getHorasPresenciales() {return horasPresenciales;}
    public void setHorasPresenciales(Integer horasPresenciales) {this.horasPresenciales = horasPresenciales;}
}
