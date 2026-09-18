package com.caeproject.cae.infraestructure.dtos.ficha;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

public class RegistrarFichaRequest {

    @NotBlank(message = "El código alfanumérico es obligatorio")
    private String codigoFicha;

    @NotNull(message = "El ID del programa es obligatorio")
    private Long programaId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private Date fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private Date fechaFin;

    public String getCodigoFicha() { return codigoFicha; }
    public void setCodigoFicha(String codigoFicha) { this.codigoFicha = codigoFicha; }

    public Long getProgramaId() { return programaId; }
    public void setProgramaId(Long programaId) { this.programaId = programaId; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
}
