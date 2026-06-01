package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.CorreoYaRegistradoException;
import com.caeproject.cae.domain.ports.exceptions.DocumentoYaRegistradoException;
import com.caeproject.cae.domain.ports.in.usuario.CrearUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.perfil_base.PerfilBase;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.PerfilBaseRepository;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

public class CrearUsuarioUseCase implements CrearUsuarioInputPort {
    private final UsuarioRepository usuarioRepository;
    private final PerfilBaseRepository perfilBaseRepository;

    public CrearUsuarioUseCase(UsuarioRepository usuarioRepository,
                               PerfilBaseRepository perfilBaseRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilBaseRepository = perfilBaseRepository;
    }

    @Override
    public Usuario crearUsuario(Usuario usuario, PerfilBase perfilBase) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new CorreoYaRegistradoException(usuario.getCorreo());
        }
        if (perfilBaseRepository.findByCC(perfilBase.getCc()).isPresent()) {
            throw new DocumentoYaRegistradoException(perfilBase.getCc());
        }

        Usuario usuarioGuardado = usuarioRepository.saveUser(usuario);

        perfilBase.setUsuarioId(usuarioGuardado.getId());
        perfilBaseRepository.save(perfilBase);

        return usuarioGuardado;
    }
}
