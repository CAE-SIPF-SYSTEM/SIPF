package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;

import java.util.List;
import java.util.Optional;

public interface ProgramacionAcademicaRepository {
 List<ProgramacionAcademica> findAll() ;
 Optional<ProgramacionAcademica>findById(Long id);
 List<ProgramacionAcademica> findByTrimestre(Long trimestreId);
 List<ProgramacionAcademica> findByUserId(Long userId);
 void eliminarProgramacionAcademica(Long id);
}
