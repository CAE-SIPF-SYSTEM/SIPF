package com.caeproject.cae.infraestructure.adapter.out.ficha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FichaJpaRepository extends JpaRepository<FichaEntity, Long> {
    boolean existsByCodigoFicha(String codigoFicha);
    List<FichaEntity> findByProgramaId(Long programaId);
}
