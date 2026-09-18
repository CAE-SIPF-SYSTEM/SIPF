package com.caeproject.cae.infraestructure.dtos.asignacioninstructor;

import java.util.List;

public class ResumenCompetenciaResponse {

    private Long competenciaId;
    private String nombreCompetencia;
    private Long totalHorasRequeridas;
    private List<InstructorSugeridoResponse> instructoresSugeridos;

    public Long getCompetenciaId() {
        return competenciaId;
    }

    public void setCompetenciaId(Long competenciaId) {
        this.competenciaId = competenciaId;
    }

    public String getNombreCompetencia() {
        return nombreCompetencia;
    }

    public void setNombreCompetencia(String nombreCompetencia) {
        this.nombreCompetencia = nombreCompetencia;
    }

    public Long getTotalHorasRequeridas() {
        return totalHorasRequeridas;
    }

    public void setTotalHorasRequeridas(Long totalHorasRequeridas) {
        this.totalHorasRequeridas = totalHorasRequeridas;
    }

    public List<InstructorSugeridoResponse> getInstructoresSugeridos() {
        return instructoresSugeridos;
    }

    public void setInstructoresSugeridos(List<InstructorSugeridoResponse> instructoresSugeridos) {
        this.instructoresSugeridos = instructoresSugeridos;
    }
}
