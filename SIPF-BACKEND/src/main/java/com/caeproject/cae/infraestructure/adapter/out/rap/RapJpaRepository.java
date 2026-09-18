package com.caeproject.cae.infraestructure.adapter.out.rap;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RapJpaRepository extends JpaRepository<RapEntity, Long> {
    List<RapEntity> findByCompetenciaId(Long competenciaId);
    boolean existsByCompetenciaId(Long competenciaId);
}
