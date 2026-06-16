package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.usuarioExceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.usuario.EditarUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

import com.caeproject.cae.application.utils.ValidacionContrasena;
import com.caeproject.cae.domain.ports.exceptions.sessionExceptions.ContrasenaInvalidaException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
            if (!ValidacionContrasena.esValida(usuario.getContrasena())) {
                throw new ContrasenaInvalidaException("La contraseña debe tener mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula, 1 número y 1 símbolo.");
            }
            usuarioExistente.setContrasena(usuario.getContrasena());
        }
        if (usuario.getRol() != null) {
            usuarioExistente.setRol(usuario.getRol());
        }

        return usuarioRepository.saveUser(usuarioExistente);
    }
}
