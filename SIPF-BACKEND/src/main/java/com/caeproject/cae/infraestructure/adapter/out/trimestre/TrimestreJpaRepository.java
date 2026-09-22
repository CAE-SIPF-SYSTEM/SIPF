package com.caeproject.cae.infraestructure.adapter.out.trimestre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrimestreJpaRepository extends JpaRepository<TrimestreEntity, Long> {
    boolean existsByFichaId(Long fichaId);
    boolean existsByFichaIdAndNumeroTrimestre(Long fichaId, Integer numeroTrimestre);
    List<TrimestreEntity> findByFichaId(Long fichaId);
}
