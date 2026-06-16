package com.caeproject.cae.infraestructure.adapter.out.ficha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FichaJpaRepository extends JpaRepository<FichaEntity, Long> {
    boolean existsByCodigoFicha(String codigoFicha);
}
