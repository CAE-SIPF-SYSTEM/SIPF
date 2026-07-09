package com.caeproject.cae.infraestructure.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "instructor_especialidad")
public class InstructorEspecialidadEntity {

    @Id
    @Column(name = "instructor_id")
    private Long usuarioId;

    @Column(name = "especialidad_id", nullable = false)
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
