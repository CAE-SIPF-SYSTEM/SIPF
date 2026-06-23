package com.caeproject.cae.infraestructure.adapter.out.perfilbase;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilBaseJpaRepository extends JpaRepository<PerfilBaseEntity, Long> {
    Optional<PerfilBaseEntity> findByDocumentoIdentidad(Long documentoIdentidad);
}
