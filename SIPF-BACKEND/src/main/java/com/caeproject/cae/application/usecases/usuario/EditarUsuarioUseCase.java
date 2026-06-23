package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.application.usecases.usuario.commands.EditarUsuarioCommand;
import com.caeproject.cae.application.utils.ValidacionContrasena;
import com.caeproject.cae.domain.ports.exceptions.sessionexceptions.ContrasenaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.usuario.EditarUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class EditarUsuarioUseCase implements EditarUsuarioInputPort {
    private final UsuarioRepository usuarioRepository;

    public EditarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario editarUsuario(EditarUsuarioCommand command, Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        if (command.getCorreo() != null) {
            usuarioExistente.setCorreo(command.getCorreo());
        }
        if (command.getContrasena() != null) {
            if (!ValidacionContrasena.esValida(command.getContrasena())) {
                throw new ContrasenaInvalidaException("La contraseña debe tener mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula, 1 número y 1 símbolo.");
            }
            usuarioExistente.setContrasena(command.getContrasena());
        }
        if (command.getRol() != null) {
            usuarioExistente.setRol(command.getRol());
        }
        if (command.getEstado() != null) {
            usuarioExistente.setEstado(command.getEstado());
        }

        return usuarioRepository.saveUser(usuarioExistente);
    }
}
