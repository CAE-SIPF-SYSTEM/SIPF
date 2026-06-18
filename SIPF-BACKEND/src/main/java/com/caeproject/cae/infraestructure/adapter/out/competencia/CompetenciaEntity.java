package com.caeproject.cae.infraestructure.adapter.out.competencia;

import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import jakarta.persistence.*;

@Entity
@Table(name = "COMPETENCIA")
public class CompetenciaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "tipo_competencia")
    private TipoCompetencia tipoCompetencia;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoCompetencia getTipoCompetencia() { return tipoCompetencia; }
    public void setTipoCompetencia(TipoCompetencia tipoCompetencia) { this.tipoCompetencia = tipoCompetencia; }
}
