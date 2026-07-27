package com.caeproject.cae.infraestructure.adapter.out.instructorespecialidad;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "instructor_especialidad")
public class InstructorEspecialidadEntity implements Persistable<Long> {

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


    @Transient
    private boolean esNuevo = true;
    
    @Override
    public Long getId() {
        return usuarioId;
    }
    @Override
    public boolean isNew() {
        return esNuevo; // 2. Retorna la bandera dinámica
    }
    @PostLoad
    @PostPersist
    public void marcarComoNoNuevo() {
        this.esNuevo = false;
    }
}
