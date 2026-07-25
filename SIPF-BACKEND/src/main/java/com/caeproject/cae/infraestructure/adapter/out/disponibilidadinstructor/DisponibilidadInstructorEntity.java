package com.caeproject.cae.infraestructure.adapter.out.disponibilidadinstructor;


import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;
import com.caeproject.cae.infraestructure.adapter.out.ubicacion.MunicipioEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class DisponibilidadInstructorEntity {

    @Id
    @GeneratedValue
    private Long usuarioId;



    @Column (name = "HorasMaximas", nullable = false)
    private Long horasMaximas;

    @Enumerated(EnumType.STRING)
    @Column(name = "jornada")
    private com.caeproject.cae.domain.ports.model.enums.Jornada jornada;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "disponibilidad_municipio",
            joinColumns = @JoinColumn(name = "disponibilidad_id"),
            inverseJoinColumns = @JoinColumn(name = "municipio_id")
    )
    private List<MunicipioEntity> municipios;

    @CollectionTable(name = "DiasDisponiblesInstructor")
    @ElementCollection(targetClass = DiasDisponibles.class)
    @Enumerated(EnumType.STRING)
    private List<DiasDisponibles> diasDisponibles;


    @Enumerated(EnumType.STRING)
    @Column(name = "jornada")
    private com.caeproject.cae.domain.ports.model.enums.Jornada jornada;

    public Long getUsuarioId() {return usuarioId;}
    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}

    public List<DiasDisponibles> getDiasDisponibles() {
        return diasDisponibles;
    }

    public void setDiasDisponibles(List<DiasDisponibles> diasDisponibles) {
        this.diasDisponibles = diasDisponibles;
    }

    public Long getHorasMaximas() {return horasMaximas;}
    public void setHorasMaximas(Long horasMaximas) {this.horasMaximas = horasMaximas;}

    public com.caeproject.cae.domain.ports.model.enums.Jornada getJornada() {return jornada;}
    public void setJornada(com.caeproject.cae.domain.ports.model.enums.Jornada jornada) {this.jornada = jornada;}

    public List<MunicipioEntity> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<MunicipioEntity> municipios) {
        this.municipios = municipios;
    }
}
