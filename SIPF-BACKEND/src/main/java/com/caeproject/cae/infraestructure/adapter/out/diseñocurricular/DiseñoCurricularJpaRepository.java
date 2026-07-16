package com.caeproject.cae.infraestructure.adapter.out.diseñocurricular;

import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiseñoCurricularJpaRepository  extends JpaRepository <DiseñoCurricularEntity,Long>{

    List<DiseñoCurricularEntity> findByProgramaId(Long programaId);
    List<DiseñoCurricularEntity> findByProgramaIdAndNumeroTrimestre(Long programaId, Integer numeroTrimestre);
    void deleteByProgramaId(Long programaId);
}
