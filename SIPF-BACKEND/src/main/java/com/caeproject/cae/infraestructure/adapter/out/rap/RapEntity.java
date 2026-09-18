package com.caeproject.cae.infraestructure.adapter.out.rap;

import jakarta.persistence.*;

@Entity
@Table(name = "RAP")
public class RapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "competencia_id", nullable = false)
    private Long competenciaId;

    @Column(nullable = false, length = 500)
    private String descripcion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCompetenciaId() { return competenciaId; }
    public void setCompetenciaId(Long competenciaId) { this.competenciaId = competenciaId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
