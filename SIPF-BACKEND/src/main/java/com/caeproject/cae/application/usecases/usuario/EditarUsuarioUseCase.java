package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.application.usecases.usuario.commands.EditarUsuarioCommand;
import com.caeproject.cae.application.utils.ValidacionContrasena;
import com.caeproject.cae.domain.ports.exceptions.sessionexceptions.ContrasenaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.usuario.EditarUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import com.caeproject.cae.domain.ports.out.AuditoriaPerfilRepository;
import com.caeproject.cae.domain.ports.model.auditoria.AuditoriaPerfil;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
public class EditarUsuarioUseCase implements EditarUsuarioInputPort {
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaPerfilRepository auditoriaPerfilRepository;

    public EditarUsuarioUseCase(UsuarioRepository usuarioRepository, AuditoriaPerfilRepository auditoriaPerfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaPerfilRepository = auditoriaPerfilRepository;
    }

    private void registrarAuditoria(Long usuarioId, String campo, String valorAnterior, String valorNuevo) {
        if (valorNuevo != null && !valorNuevo.equals(valorAnterior)) {
            AuditoriaPerfil auditoria = new AuditoriaPerfil();
            auditoria.setUsuarioId(usuarioId);
            auditoria.setCampoModificado(campo);
            auditoria.setValorAnterior(valorAnterior != null ? valorAnterior : "N/A");
            auditoria.setValorNuevo(valorNuevo);
            auditoria.setFechaModificacion(LocalDateTime.now());
            auditoriaPerfilRepository.guardarAuditoria(auditoria);
        }
    }

    @Override
    public Usuario editarUsuario(EditarUsuarioCommand command, Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));

        if (command.getCorreo() != null) {
            registrarAuditoria(id, "correo", usuarioExistente.getCorreo(), command.getCorreo());
            usuarioExistente.setCorreo(command.getCorreo());
        }
        if (command.getContrasena() != null) {
            if (!ValidacionContrasena.esValida(command.getContrasena())) {
                throw new ContrasenaInvalidaException("La contraseña debe tener mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula, 1 número y 1 símbolo.");
            }
            registrarAuditoria(id, "contrasena", "***", "***"); // No guardamos el hash
            usuarioExistente.setContrasena(command.getContrasena());
        }
        if (command.getRol() != null) {
            registrarAuditoria(id, "rol", usuarioExistente.getRol() != null ? usuarioExistente.getRol().name() : null, command.getRol().name());
            usuarioExistente.setRol(command.getRol());
        }
        if (command.getEstado() != null) {
            registrarAuditoria(id, "estado", String.valueOf(usuarioExistente.isEstado()), String.valueOf(command.getEstado()));
            usuarioExistente.setEstado(command.getEstado());
        }

        if (command.getNombre() != null || command.getApellido() != null || command.getTelefono() != null) {
            if (usuarioExistente.getPerfilBase() == null) {
                com.caeproject.cae.domain.ports.model.perfilbase.PerfilBase perfil = new com.caeproject.cae.domain.ports.model.perfilbase.PerfilBase();
                perfil.setUsuarioId(usuarioExistente.getId());
                usuarioExistente.setPerfilBase(perfil);
            }
            if (command.getNombre() != null) {
                registrarAuditoria(id, "nombre", usuarioExistente.getPerfilBase().getNombre(), command.getNombre());
                usuarioExistente.getPerfilBase().setNombre(command.getNombre());
            }
            if (command.getApellido() != null) {
                registrarAuditoria(id, "apellido", usuarioExistente.getPerfilBase().getApellido(), command.getApellido());
                usuarioExistente.getPerfilBase().setApellido(command.getApellido());
            }
            if (command.getTelefono() != null) {
                String telAnt = usuarioExistente.getPerfilBase().getTelefono() != null ? usuarioExistente.getPerfilBase().getTelefono().toString() : "N/A";
                registrarAuditoria(id, "telefono", telAnt, command.getTelefono().toString());
                usuarioExistente.getPerfilBase().setTelefono(command.getTelefono());
            }
        }

        return usuarioRepository.saveUser(usuarioExistente);
    }
}
