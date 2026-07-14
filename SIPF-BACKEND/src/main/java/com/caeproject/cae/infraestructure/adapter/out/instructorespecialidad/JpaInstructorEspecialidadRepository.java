package com.caeproject.cae.infraestructure.adapter.out.instructorespecialidad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaInstructorEspecialidadRepository extends JpaRepository<InstructorEspecialidadEntity, Long> {
    Optional<InstructorEspecialidadEntity> findByUsuarioId(Long usuarioId);
    List<InstructorEspecialidadEntity> findByEspecialidadId(Long especialidadId);
    
    @Transactional
    void deleteByUsuarioId(Long usuarioId);
}
