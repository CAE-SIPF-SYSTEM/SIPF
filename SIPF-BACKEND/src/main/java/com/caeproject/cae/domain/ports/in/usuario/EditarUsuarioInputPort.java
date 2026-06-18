package com.caeproject.cae.domain.ports.in.usuario;

import com.caeproject.cae.application.usecases.usuario.commands.EditarUsuarioCommand;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;

public interface EditarUsuarioInputPort {
    Usuario editarUsuario(EditarUsuarioCommand command, Long id);
}
