package com.caeproject.cae.domain.ports.in.competenciaespecialidad;

import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;

import java.util.List;

public interface ListarEspecialidadesCompetenciaInputPort {
    List<CompetenciaEspecialidad> listarEspecialidadesCompetencias();
}
