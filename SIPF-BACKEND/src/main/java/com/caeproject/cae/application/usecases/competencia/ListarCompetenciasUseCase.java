package com.caeproject.cae.application.usecases.competencia;

import com.caeproject.cae.domain.ports.in.competencia.ListarCompetenciasInputPort;
import com.caeproject.cae.domain.ports.model.Competencia;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;

import java.util.List;

public class ListarCompetenciasUseCase implements ListarCompetenciasInputPort {

    final CompetenciaRepository competenciaRepository;

    public ListarCompetenciasUseCase (CompetenciaRepository competenciaRepository){
        this.competenciaRepository = competenciaRepository;

    }

    @Override
    public List<Competencia> listarCompetencia() {
        return competenciaRepository.findAll();
    }
}
