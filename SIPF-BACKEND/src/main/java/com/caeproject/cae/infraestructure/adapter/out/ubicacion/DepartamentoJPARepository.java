package com.caeproject.cae.infraestructure.adapter.out.ubicacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartamentoJPARepository extends JpaRepository<DepartamentoEntity,Long> {
    List<DepartamentoEntity> findByNombre(String nombre);
}
