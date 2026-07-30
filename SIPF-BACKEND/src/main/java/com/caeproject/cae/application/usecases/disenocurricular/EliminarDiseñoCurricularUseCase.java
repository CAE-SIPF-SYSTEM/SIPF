package com.caeproject.cae.application.usecases.disenocurricular;

import com.caeproject.cae.domain.ports.in.disenocurricular.EliminarDiseñoCurricularInputPort;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;

public class EliminarDiseñoCurricularUseCase implements EliminarDiseñoCurricularInputPort {

    public final DiseñoCurricularRepository disenoCurricularRepository;

    public EliminarDiseñoCurricularUseCase(DiseñoCurricularRepository disenoCurricularRepository) {
        this.disenoCurricularRepository = disenoCurricularRepository;
    }

    @Override
    public void eliminarDiseñoCurricular(Long id) {
        disenoCurricularRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No se encontro el id" + id));
        disenoCurricularRepository.eliminarDiseñoCurricular(id);
    }
}
