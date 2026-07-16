package com.caeproject.cae.application.usecases.CompetenciaEspecialidad;

import com.caeproject.cae.domain.ports.exceptions.competenciaespecialidadexception.CompetenciaEspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.in.CompetenciaEspecialidad.DesasignarEspecialidadCompetenciaInputPort;
import com.caeproject.cae.domain.ports.out.CompetenciaEspecialidadRepository;

public class DesasignarEspecialidadCompetenciaUseCase implements DesasignarEspecialidadCompetenciaInputPort {

    private final CompetenciaEspecialidadRepository competenciaEspecialidadRepository;

    public DesasignarEspecialidadCompetenciaUseCase(CompetenciaEspecialidadRepository competenciaEspecialidadRepository) {
        this.competenciaEspecialidadRepository = competenciaEspecialidadRepository;
    }

    @Override
    public void desasignarEspecialidadCompetencia(Long competenciaId) {
        if (competenciaEspecialidadRepository.findByCompetenciaId(competenciaId).isEmpty()) {
            throw new CompetenciaEspecialidadNoEncontradaException("Especialidad de la competencia no encontrada");
        }
        competenciaEspecialidadRepository.eliminarCompetenciaEspecialidad(competenciaId);
    }
}
