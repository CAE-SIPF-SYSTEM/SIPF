package com.caeproject.cae.infraestructure.repositories;

import com.caeproject.cae.infraestructure.entities.EspecialidadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaEspecialidadRepository extends JpaRepository<EspecialidadEntity, Long> {
    List<EspecialidadEntity> findByNombreEspecialidadContainingIgnoreCase(String nombreEspecialidad);
    boolean existsByNombreEspecialidad(String nombreEspecialidad);
}
