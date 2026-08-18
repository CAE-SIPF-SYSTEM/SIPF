package com.caeproject.cae.infraestructure.adapter.out.programacionacademica;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramacionAcademicaJpaRepository extends JpaRepository<ProgramacionAcademicaEntity, Long> {
    List<ProgramacionAcademicaEntity> findByTrimestreId(Long trimestreId);
    List<ProgramacionAcademicaEntity> findByUsuarioId(Long usuarioId);
    List<ProgramacionAcademicaEntity> findByFichaIdAndTrimestreId(Long fichaId, Long trimestreId);
    boolean existsByRapIdAndTrimestreId(Long rapId, Long trimestreId);
    boolean existsByRapIdAndFichaIdAndTrimestreId(Long rapId, Long fichaId, Long trimestreId);
    List<ProgramacionAcademicaEntity> findByUsuarioIdAndTrimestreId(Long usuarioId, Long trimestreId);
}
