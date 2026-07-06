package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.usuario.InhabilitarUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

public class InhabilitarUseCase implements InhabilitarUsuarioInputPort {
    private final UsuarioRepository usuarioRepository;

    public InhabilitarUseCase (UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario inhabilitarUsuario(Long id ) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
        usuario.setEstado(false);
        return usuarioRepository.saveUser(usuario);
    }
}
