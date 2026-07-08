package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.domain.ports.model.Trimestre;

import java.util.List;
import java.util.Optional;

public interface TrimestreRepository {
    List<Trimestre> findAll();
    Optional<Trimestre>findById(Long Id);
    void eliminarTrimestre(Long Id);
    List<Trimestre>findByFicha(Long fichaId);
    Trimestre savetrimestre (Trimestre trimestre);
    boolean existsByFicha(Long fichaId);
}
