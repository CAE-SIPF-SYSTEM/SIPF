package com.caeproject.cae.infraestructure.adapter.out.programacionAcademica;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgramacionAcademicaJpaRepository extends JpaRepository<ProgramacionAcademicaEntity, Long> {
    List<ProgramacionAcademicaEntity> findByTrimestreId(Long trimestreId);
    Optional<ProgramacionAcademicaEntity> findByUserId(Long userId);
}
