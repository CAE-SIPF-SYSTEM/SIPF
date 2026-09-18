package com.caeproject.cae.infraestructure.dtos.instructorespecialidad;

public class InstructorEspecialidadRequestDTO {
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
