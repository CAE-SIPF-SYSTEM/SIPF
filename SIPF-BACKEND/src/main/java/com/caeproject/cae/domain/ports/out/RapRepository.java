package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.rap.Rap;

import java.util.List;
import java.util.Optional;

public interface RapRepository {
    List<Rap> findAll();
    Optional<Rap> findById(Long id);
    void eliminarRap(Long id);
    List<Rap>findByCompetencia(Long competenciaId);
    boolean existByCompetenciaId(Long competenciaId);
    Rap saveRap(Rap rap);
}
