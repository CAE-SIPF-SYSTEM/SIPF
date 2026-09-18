package com.caeproject.cae.application.usecases.competencia;

import com.caeproject.cae.domain.ports.exceptions.competenciaexception.CompetenciaNoEncontradaException;
import com.caeproject.cae.domain.ports.in.competencia.ObtenerCompetenciaInputPort;
import com.caeproject.cae.domain.ports.model.Competencia;
import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;

import java.util.List;

public class ObtenerCompetenciaUseCase implements ObtenerCompetenciaInputPort {

    final CompetenciaRepository competenciaRepository;

    public ObtenerCompetenciaUseCase (CompetenciaRepository competenciaRepository){
        this.competenciaRepository = competenciaRepository;

    }

    @Override
    public Competencia obtenerCompetencia(Long id) {
        return competenciaRepository.findById(id)
                .orElseThrow(()-> new CompetenciaNoEncontradaException(id));

    }

    @Override
    public List<Competencia> obtenerPorTipoCompetencia(TipoCompetencia tipoCompetencia) {
        List<Competencia> competencias = competenciaRepository.findByTipoCompetencia(tipoCompetencia);
        return competencias;
    }
}
