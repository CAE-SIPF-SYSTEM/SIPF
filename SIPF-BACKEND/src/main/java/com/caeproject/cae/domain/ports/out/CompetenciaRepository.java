package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.competencia.Competencia;
import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;

import java.util.List;
import java.util.Optional;

public interface CompetenciaRepository {
    Optional<Competencia> findById(Long id);
    List <Competencia>findAll();
    List<Competencia>findByTipoCompetencia(TipoCompetencia tipoCompetencia);
    Competencia saveCompetencia(Competencia competencia);
    void eliminarCompetencia(Long id);
    boolean existByName(String nombre);
    Optional<Competencia> findByCodigo(String codigo);

}
