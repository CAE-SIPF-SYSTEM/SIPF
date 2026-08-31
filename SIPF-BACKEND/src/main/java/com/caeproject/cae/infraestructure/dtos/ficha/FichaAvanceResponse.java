package com.caeproject.cae.infraestructure.dtos.ficha;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FichaAvanceResponse {
    private Long fichaId;
    private String codigoFicha;
    private Long programaId;
    private String programaNombre;
    private String jornada;
    private int rapsVistos;
    private int rapsPendientes;
    private int totalRaps;
    private int porcentajeAvance;
    private String estado;
}
