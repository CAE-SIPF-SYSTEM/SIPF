package com.caeproject.cae.application.usecases.diseñoCurricular;

import com.caeproject.cae.domain.ports.in.diseñoCurricular.ListarDiseñoCurricularInputPort;
import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;

import java.util.List;

public class LIstarDiseñoCurricularUseCase implements ListarDiseñoCurricularInputPort {

    private final DiseñoCurricularRepository diseñoCurricularRepository;

    public LIstarDiseñoCurricularUseCase(DiseñoCurricularRepository diseñoCurricularRepository) {
        this.diseñoCurricularRepository = diseñoCurricularRepository;
    }

    @Override
    public List<DiseñoCurricular> listarDiseñoCurricular() {
        return diseñoCurricularRepository.findAll();
    }
}
