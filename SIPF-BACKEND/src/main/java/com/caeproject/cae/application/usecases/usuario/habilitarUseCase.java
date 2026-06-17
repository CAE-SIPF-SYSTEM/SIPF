package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.usuarioExceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.usuario.habilitarUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

public class habilitarUseCase implements habilitarUsuarioInputPort {
    private final UsuarioRepository usuarioRepository;

    public habilitarUseCase(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario habilitarUsuario(Long id ) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
        usuario.setEstado(true);
        return usuarioRepository.saveUser(usuario);
    }
}
