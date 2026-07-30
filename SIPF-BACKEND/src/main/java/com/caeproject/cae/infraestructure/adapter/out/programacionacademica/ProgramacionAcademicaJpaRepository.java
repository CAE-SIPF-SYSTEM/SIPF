package com.caeproject.cae.infraestructure.adapter.out.programacionacademica;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramacionAcademicaJpaRepository extends JpaRepository<ProgramacionAcademicaEntity, Long> {
    List<ProgramacionAcademicaEntity> findByTrimestreId(Long trimestreId);
    List<ProgramacionAcademicaEntity> findByUsuarioId(Long usuarioId);
    boolean existsByRapIdAndTrimestreId(Long rapId, Long trimestreId);
}
