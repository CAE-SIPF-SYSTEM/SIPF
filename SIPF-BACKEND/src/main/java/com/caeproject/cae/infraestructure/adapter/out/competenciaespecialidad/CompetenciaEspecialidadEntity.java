package com.caeproject.cae.infraestructure.adapter.out.competenciaespecialidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "competencia_especialidad")
public class CompetenciaEspecialidadEntity {

    @Id
    @Column(name = "competencia_id")
    private Long competenciaId;

    @Column(name = "especialidad_id", nullable = false)
    private Long especialidadId;

    public CompetenciaEspecialidadEntity() {
    }

    public CompetenciaEspecialidadEntity(Long competenciaId, Long especialidadId) {
        this.competenciaId = competenciaId;
        this.especialidadId = especialidadId;
    }

    public Long getCompetenciaId() {
        return competenciaId;
    }

    public void setCompetenciaId(Long competenciaId) {
        this.competenciaId = competenciaId;
    }

    public Long getEspecialidadId() {
        return especialidadId;
    }

    public void setEspecialidadId(Long especialidadId) {
        this.especialidadId = especialidadId;
    }
}
