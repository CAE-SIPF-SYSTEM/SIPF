package com.caeproject.cae.infraestructure.adapter.out.competenciaespecialidad;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCompetenciaEspecialidadRepository extends JpaRepository<CompetenciaEspecialidadEntity, Long> {
    Optional<CompetenciaEspecialidadEntity> findByCompetenciaId(Long competenciaId);
    List<CompetenciaEspecialidadEntity> findByEspecialidadId(Long especialidadId);
    
    @Transactional
    void deleteByCompetenciaId(Long competenciaId);
}
