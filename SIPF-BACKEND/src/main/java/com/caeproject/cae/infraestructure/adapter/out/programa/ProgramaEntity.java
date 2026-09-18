package com.caeproject.cae.infraestructure.adapter.out.programa;

import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import com.caeproject.cae.infraestructure.adapter.out.ubicacion.MunicipioEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "PROGRAMA")
public class ProgramaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @ManyToOne
    private MunicipioEntity municipio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelFormacion nivelFormacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Jornada jornada;

    @Column(name = "duracion_practicas", nullable = false)
    private Integer duracionpracticas;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public MunicipioEntity getMunicipio() {
        return municipio;
    }

    public void setMunicipio(MunicipioEntity municipio) {
        this.municipio = municipio;
    }

    public NivelFormacion getNivelFormacion() { return nivelFormacion; }
    public void setNivelFormacion(NivelFormacion nivelFormacion) { this.nivelFormacion = nivelFormacion; }

    public Jornada getJornada() { return jornada; }
    public void setJornada(Jornada jornada) { this.jornada = jornada; }

    public Integer getDuracionpracticas() { return duracionpracticas; }
    public void setDuracionpracticas(Integer duracionpracticas) { this.duracionpracticas = duracionpracticas; }
}
