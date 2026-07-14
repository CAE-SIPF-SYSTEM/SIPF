package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.Rap;

import java.util.List;
import java.util.Optional;

public interface RapRepository {
    List<Rap> findAll();
    Optional<Rap> findById(Long id);
    List<Rap> findByCompetencia(Long competenciaId);
    Rap saveRap(Rap rap);
    void eliminarRap(Long id);
    boolean existByCompetenciaId(Long competenciaId);
}
