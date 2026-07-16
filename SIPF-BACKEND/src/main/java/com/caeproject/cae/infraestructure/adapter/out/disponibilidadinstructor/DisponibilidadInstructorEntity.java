package com.caeproject.cae.infraestructure.adapter.out.disponibilidadinstructor;


import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class DisponibilidadInstructorEntity {

    @Id
    @GeneratedValue
    private Long usuarioId;

    @ElementCollection(targetClass = DiasDisponibles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "instructor_dias", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "dia_disponible")
    private List<DiasDisponibles> diasDisponibles;

    @Column (name = "HorasMaximas", nullable = false)
    private Long horasMaximas;

    @Enumerated(EnumType.STRING)
    @Column(name = "jornada")
    private com.caeproject.cae.domain.ports.model.enums.Jornada jornada;

    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}

    public List<DiasDisponibles> getDiasDisponibles() {return diasDisponibles;}
    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) {this.diasDisponibles = diasDisponibles;}

    public Long getHorasMaximas() {return horasMaximas;}
    public void setHorasMaximas(Long horasMaximas) {this.horasMaximas = horasMaximas;}

    public com.caeproject.cae.domain.ports.model.enums.Jornada getJornada() {return jornada;}
    public void setJornada(com.caeproject.cae.domain.ports.model.enums.Jornada jornada) {this.jornada = jornada;}
}
