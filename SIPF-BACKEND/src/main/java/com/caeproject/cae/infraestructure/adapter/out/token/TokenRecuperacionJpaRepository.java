package com.caeproject.cae.infraestructure.adapter.out.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacionJpaRepository extends JpaRepository<TokenRecuperacionEntity, Long> {

    Optional<TokenRecuperacionEntity> findByToken(String token);

    void deleteByUsuarioId(Long usuarioId);
}
