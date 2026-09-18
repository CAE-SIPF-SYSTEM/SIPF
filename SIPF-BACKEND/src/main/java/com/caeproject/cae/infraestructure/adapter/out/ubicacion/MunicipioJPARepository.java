package com.caeproject.cae.infraestructure.adapter.out.ubicacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipioJPARepository  extends JpaRepository<MunicipioEntity,Long> {
    List<MunicipioEntity> findByNombre(String nombre);
}
