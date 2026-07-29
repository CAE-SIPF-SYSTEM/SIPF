package com.caeproject.cae.infraestructure.dtos.programacionacademica;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CrearProgramacionRequest {

    @NotNull
    private Long rapId;

    @NotNull
    private Long trimestreId;

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long programaId;

    public Long getProgramaId() {
        return programaId;
    }

    public void setProgramaId(Long programaId) {
        this.programaId = programaId;
    }

    public Long getRapId() {return rapId;}
    public void setRapId(Long rapId) {this.rapId = rapId;}

    public Long getTrimestreId() {return trimestreId;}
    public void setTrimestreId(Long trimestreId) {this.trimestreId = trimestreId;}

    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}
}
