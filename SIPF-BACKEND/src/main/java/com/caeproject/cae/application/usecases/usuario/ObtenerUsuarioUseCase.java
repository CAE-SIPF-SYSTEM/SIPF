package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.usuarioExceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.usuario.ObtenerUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class ObtenerUsuarioUseCase implements ObtenerUsuarioInputPort {
    private final UsuarioRepository usuarioRepository;

    public ObtenerUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario obtenerUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    @Override
    public Optional<Usuario> porCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public List<Usuario> porRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }
}
