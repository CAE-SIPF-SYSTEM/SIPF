package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.ficha.Ficha;
import java.util.List;
import java.util.Optional;

public interface FichaRepository {
    boolean existByCodigoFicha(String codigoFicha);
    Ficha saveFicha(Ficha ficha);
    Optional<Ficha> findById(Long id);
    List<Ficha> findAll();
    void deleteFicha(Long id);
    List<Ficha> findByProgramaId(Long programaId);
}
