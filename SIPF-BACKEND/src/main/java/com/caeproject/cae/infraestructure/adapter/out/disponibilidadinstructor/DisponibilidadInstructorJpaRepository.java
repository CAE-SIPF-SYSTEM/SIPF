package com.caeproject.cae.infraestructure.adapter.out.disponibilidadinstructor;

import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DisponibilidadInstructorJpaRepository extends JpaRepository<DisponibilidadInstructorEntity, Long> {
    List<DisponibilidadInstructorEntity> findByDiasDisponibles(DiasDisponibles diasDisponibles);
    boolean existsByUsuarioId(Long usuarioId);
}
