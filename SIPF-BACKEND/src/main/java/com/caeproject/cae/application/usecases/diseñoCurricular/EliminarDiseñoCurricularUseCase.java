package com.caeproject.cae.application.usecases.diseñoCurricular;

import com.caeproject.cae.domain.ports.in.diseñoCurricular.EliminarDiseñoCurricularInputPort;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;

public class EliminarDiseñoCurricularUseCase implements EliminarDiseñoCurricularInputPort {

    public final DiseñoCurricularRepository diseñoCurricularRepository;

    public EliminarDiseñoCurricularUseCase(DiseñoCurricularRepository diseñoCurricularRepository) {
        this.diseñoCurricularRepository = diseñoCurricularRepository;
    }

    @Override
    public void eliminarDiseñoCurricular(Long id) {
        diseñoCurricularRepository.findyById(id)
                .orElseThrow(()-> new RuntimeException("No se encontro el id" + id));
        diseñoCurricularRepository.eliminarDiseñoCurricular(id);
    }
}
