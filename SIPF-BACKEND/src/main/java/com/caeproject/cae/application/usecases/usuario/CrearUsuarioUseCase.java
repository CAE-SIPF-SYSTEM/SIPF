package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.application.usecases.usuario.commands.CrearUsuarioCommand;
import com.caeproject.cae.application.utils.ValidacionContrasena;
import com.caeproject.cae.domain.ports.exceptions.instructorexception.DocumentoYaRegistradoException;
import com.caeproject.cae.domain.ports.exceptions.sessionexceptions.ContrasenaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.CorreoYaRegistradoException;
import com.caeproject.cae.domain.ports.in.usuario.CrearUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.PerfilBase;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.out.PerfilBaseRepository;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
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
    public Usuario crearUsuario(CrearUsuarioCommand command) {
        if (!ValidacionContrasena.esValida(command.getContrasena())) {
            throw new ContrasenaInvalidaException("La contraseña debe tener mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula, 1 número y 1 símbolo.");
        }
        
        if (usuarioRepository.findByCorreo(command.getCorreo()).isPresent()) {
            throw new CorreoYaRegistradoException(command.getCorreo());
        }
        if (perfilBaseRepository.findByCC(command.getDocumentoIdentidad()).isPresent()) {
            throw new DocumentoYaRegistradoException(command.getDocumentoIdentidad());
        }

        Usuario usuario = new Usuario();
        usuario.setCorreo(command.getCorreo());
        usuario.setContrasena(command.getContrasena());
        usuario.setRol(command.getRol());
        usuario.setEstado(true); // Regla por defecto movida al caso de uso

        Usuario usuarioGuardado = usuarioRepository.saveUser(usuario);

        PerfilBase perfilBase = new PerfilBase();
        perfilBase.setNombre(command.getNombre());
        perfilBase.setApellido(command.getApellido());
        perfilBase.setCc(command.getDocumentoIdentidad());
        perfilBase.setTelefono(command.getTelefono());
        perfilBase.setTipoContrato(command.getTipoContrato());
        perfilBase.setUsuarioId(usuarioGuardado.getId());

        perfilBaseRepository.save(perfilBase);
        usuarioGuardado.setPerfilBase(perfilBase);

        return usuarioGuardado;
    }
}
