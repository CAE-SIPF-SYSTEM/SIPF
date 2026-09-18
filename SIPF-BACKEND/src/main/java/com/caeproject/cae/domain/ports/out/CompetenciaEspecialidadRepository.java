package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;

import java.util.List;
import java.util.Optional;

public interface CompetenciaEspecialidadRepository {
    List<CompetenciaEspecialidad> findAll();
    Optional<CompetenciaEspecialidad> findByCompetenciaId(Long competenciaId);
    List<CompetenciaEspecialidad> findByEspecialidadId(Long especialidadId);
    CompetenciaEspecialidad saveCompetenciaEspecialidad(CompetenciaEspecialidad competenciaEspecialidad);
    void eliminarCompetenciaEspecialidad(Long competenciaId);
}
