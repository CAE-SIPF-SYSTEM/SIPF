package com.caeproject.cae.application.usecases.disenocurricular;

import com.caeproject.cae.domain.ports.in.disenocurricular.ListarDiseñoCurricularInputPort;
import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;

import java.util.List;

public class LIstarDiseñoCurricularUseCase implements ListarDiseñoCurricularInputPort {

    private final DiseñoCurricularRepository disenoCurricularRepository;

    public LIstarDiseñoCurricularUseCase(DiseñoCurricularRepository disenoCurricularRepository) {
        this.disenoCurricularRepository = disenoCurricularRepository;
    }

    @Override
    public List<DiseñoCurricular> listarDiseñoCurricular() {
        return disenoCurricularRepository.findAll();
    }
}
