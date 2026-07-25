package com.caeproject.cae.infraestructure.dtos.programacionacademica;

public class ProgramacionAcademicaResponse {
    private Long rapId;
    private Long trimestreId;
    private Long usuarioId;

    public Long getRapId() {return rapId;}
    public void setRapId(Long rapId) {this.rapId = rapId;}

    public Long getTrimestreId() {return trimestreId;}
    public void setTrimestreId(Long trimestreId) {this.trimestreId = trimestreId;}

    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}
}
