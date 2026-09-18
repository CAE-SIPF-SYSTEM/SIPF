package com.caeproject.cae.application.usecases.competencia;

import com.caeproject.cae.domain.ports.exceptions.competenciaexception.CompetenciaNoEncontradaException;
import com.caeproject.cae.domain.ports.in.competencia.EliminarCompetenciaInputPort;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;

public class EliminarCompetenciaUseCase implements EliminarCompetenciaInputPort {
    final CompetenciaRepository competenciaRepository;

    public EliminarCompetenciaUseCase (CompetenciaRepository competenciaRepository){
        this.competenciaRepository = competenciaRepository;

    }

    @Override
    public void eliminarCompetencia(Long id) {
        competenciaRepository.findById(id)
                .orElseThrow(() -> new CompetenciaNoEncontradaException(id));
        competenciaRepository.eliminarCompetencia(id);
    }
}
