package com.caeproject.cae.infraestructure.adapter.out.ficha;

import com.caeproject.cae.infraestructure.adapter.out.programa.ProgramaEntity;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FICHA")
public class FichaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_ficha", unique = true, nullable = false)
    private String codigoFicha;

    @Column(name = "programa_id", nullable = false)
    private Long programaId;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_inicio", nullable = false)
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_fin", nullable = false)
    private Date fechaFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id", insertable = false, updatable = false)
    private ProgramaEntity programa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoFicha() { return codigoFicha; }
    public void setCodigoFicha(String codigoFicha) { this.codigoFicha = codigoFicha; }

    public Long getProgramaId() { return programaId; }
    public void setProgramaId(Long programaId) { this.programaId = programaId; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public ProgramaEntity getPrograma() { return programa; }
    public void setPrograma(ProgramaEntity programa) { this.programa = programa; }
}
