package com.caeproject.cae.domain.ports.in.usuario;

import com.caeproject.cae.application.usecases.usuario.commands.CrearUsuarioCommand;
import com.caeproject.cae.domain.ports.model.Usuario;

public interface CrearUsuarioInputPort {
    Usuario crearUsuario (CrearUsuarioCommand command);
}
