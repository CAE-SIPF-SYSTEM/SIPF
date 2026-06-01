package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.usuario.EditarUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

public class EditarUsuarioUseCase implements EditarUsuarioInputPort {
    private final UsuarioRepository usuarioRepository;

    public EditarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario editarUsuario(Usuario usuario, Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        if (usuario.getCorreo() != null) {
            usuarioExistente.setCorreo(usuario.getCorreo());
        }
        if (usuario.getContrasena() != null) {
            usuarioExistente.setContrasena(usuario.getContrasena());
        }
        if (usuario.getRol() != null) {
            usuarioExistente.setRol(usuario.getRol());
        }

        return usuarioRepository.saveUser(usuarioExistente);
    }
}
