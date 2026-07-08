package com.caeproject.cae.infraestructure.adapter.out.trimestre;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "trimestres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrimestreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ficha_id", nullable = false)
    private Long fichaId;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "numero_trimestre", nullable = false)
    private Integer numeroTrimestre;

    @Column(name = "fecha_inicio", nullable = false)
    private Date fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private Date fechaFin;

}
