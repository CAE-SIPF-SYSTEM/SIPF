package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.usuarioExceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.usuario.EliminarUsuarioInputPort;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

public class EliminarUsuarioUseCase implements EliminarUsuarioInputPort {
    private final UsuarioRepository usuarioRepository;

    public EliminarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        usuarioRepository.deleteUser(id);
    }
}
