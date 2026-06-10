package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.CorreoYaRegistradoException;
import com.caeproject.cae.domain.ports.exceptions.DocumentoYaRegistradoException;
import com.caeproject.cae.domain.ports.in.usuario.CrearUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.perfil_base.PerfilBase;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.PerfilBaseRepository;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

import com.caeproject.cae.application.utils.ValidacionContrasena;
import com.caeproject.cae.domain.ports.exceptions.ContrasenaInvalidaException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
        if (!ValidacionContrasena.esValida(usuario.getContrasena())) {
            throw new ContrasenaInvalidaException("La contraseña debe tener mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula, 1 número y 1 símbolo.");
        }
        
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
