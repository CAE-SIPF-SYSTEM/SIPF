package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.DiseñoCurricular;

import java.util.List;
import java.util.Optional;

public interface DiseñoCurricularRepository {
    List<DiseñoCurricular> findAll();
    Optional<DiseñoCurricular> findyById(Long id);
    void eliminarDiseñoCurricular(Long id);
    DiseñoCurricular saveDiseñoCurricular(DiseñoCurricular diseñoCurricular);
}
