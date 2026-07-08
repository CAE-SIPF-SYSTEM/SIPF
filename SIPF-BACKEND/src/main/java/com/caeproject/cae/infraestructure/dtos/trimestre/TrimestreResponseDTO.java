package com.caeproject.cae.infraestructure.dtos.trimestre;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class TrimestreResponseDTO {
    private Long id;
    private Long fichaId;
    private Integer anio;
    private Integer numeroTrimestre;
    private Date fechaInicio;
    private Date fechaFin;
}
