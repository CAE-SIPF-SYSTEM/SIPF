package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.TokenRecuperacion;

import java.util.Optional;

public interface TokenRecuperacionRepository {

    TokenRecuperacion guardar(TokenRecuperacion token);

    Optional<TokenRecuperacion> buscarPorToken(String token);

    void eliminarPorUsuarioId(Long usuarioId);

    void eliminar(Long id);
}
