package com.caeproject.cae.infraestructure.adapter.out.competencia;

import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompetenciaJpaRepository extends JpaRepository<CompetenciaEntity, Long> {
    List<CompetenciaEntity> findByTipoCompetencia(TipoCompetencia tipoCompetencia);
    boolean existsByNombre(String nombre);
    Optional<CompetenciaEntity> findByCodigo(String codigo);
}
