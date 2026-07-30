package com.caeproject.cae.application.usecases.especialidadinstructor.commands;

public class AsignarEspecialidadInstructorCommand {
    private Long usuarioId;
    private Long especialidadId;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(Long especialidadId) {
        this.especialidadId = especialidadId;
    }
}
