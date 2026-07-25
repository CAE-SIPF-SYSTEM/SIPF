package com.caeproject.cae.infraestructure.adapter.out.programa;

import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramaJpaRepository extends JpaRepository<ProgramaEntity, Long> {
    boolean existsByNombre(String nombre);
    List<ProgramaEntity> findByNivelFormacion(NivelFormacion nivelFormacion);
    List<ProgramaEntity> findByJornada(Jornada jornada);
    List<ProgramaEntity> findByMunicipio_Nombre(String nombre);
}
