package com.caeproject.cae.infraestructure.adapter.out.token;

import com.caeproject.cae.domain.ports.model.TokenRecuperacion;
import com.caeproject.cae.domain.ports.out.TokenRecuperacionRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class TokenRecuperacionJpaAdapter implements TokenRecuperacionRepository {

    private final TokenRecuperacionJpaRepository jpaRepository;

    public TokenRecuperacionJpaAdapter(TokenRecuperacionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public TokenRecuperacion guardar(TokenRecuperacion token) {
        TokenRecuperacionEntity entity = toEntity(token);
        TokenRecuperacionEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<TokenRecuperacion> buscarPorToken(String token) {
        return jpaRepository.findByToken(token).map(this::toDomain);
    }

    @Override
    @Transactional
    public void eliminarPorUsuarioId(Long usuarioId) {
        jpaRepository.deleteByUsuarioId(usuarioId);
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }

    private TokenRecuperacionEntity toEntity(TokenRecuperacion domain) {
        TokenRecuperacionEntity entity = new TokenRecuperacionEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setUsuarioId(domain.getUsuarioId());
        entity.setFechaExpiracion(domain.getFechaExpiracion());
        return entity;
    }

    private TokenRecuperacion toDomain(TokenRecuperacionEntity entity) {
        TokenRecuperacion domain = new TokenRecuperacion();
        domain.setId(entity.getId());
        domain.setToken(entity.getToken());
        domain.setUsuarioId(entity.getUsuarioId());
        domain.setFechaExpiracion(entity.getFechaExpiracion());
        return domain;
    }
}
