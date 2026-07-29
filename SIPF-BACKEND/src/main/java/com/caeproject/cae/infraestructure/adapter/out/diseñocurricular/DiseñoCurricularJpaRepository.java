package com.caeproject.cae.infraestructure.adapter.out.diseñocurricular;

import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiseñoCurricularJpaRepository  extends JpaRepository <DiseñoCurricularEntity,Long>{

    List<DiseñoCurricularEntity> findByProgramaId(Long programaId);
    List<DiseñoCurricularEntity> findByProgramaIdAndNumeroTrimestre(Long programaId, Integer numeroTrimestre);
    List<DiseñoCurricularEntity> findByRapId(Long rapId);
    Optional<DiseñoCurricularEntity> findByProgramaIdAndRapId(Long programaId, Long rapId);
    void deleteByProgramaId(Long programaId);

    @Query("SELECT SUM(d.horaspresenciales) FROM DiseñoCurricularEntity d, RapEntity r " +
            "WHERE d.rapId = r.id " +
            "AND r.competenciaId = :competenciaId " +
            "AND d.programaId = :programaId")
    Integer sumarHorasPorCompetenciaYPrograma(@Param("competenciaId") Long competenciaId, @Param("programaId") Long programaId);
}
