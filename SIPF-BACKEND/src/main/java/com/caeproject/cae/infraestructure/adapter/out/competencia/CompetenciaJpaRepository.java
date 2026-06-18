package com.caeproject.cae.infraestructure.adapter.out.competencia;

import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetenciaJpaRepository extends JpaRepository<CompetenciaEntity, Long> {
    List<CompetenciaEntity> findByTipoCompetencia(TipoCompetencia tipoCompetencia);
    boolean existsByNombre(String nombre);
}
