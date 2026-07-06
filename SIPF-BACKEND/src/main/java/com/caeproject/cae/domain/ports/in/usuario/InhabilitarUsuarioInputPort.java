package com.caeproject.cae.domain.ports.in.usuario;

import com.caeproject.cae.domain.ports.model.Usuario;

public interface InhabilitarUsuarioInputPort {
    Usuario inhabilitarUsuario(Long  id);
}
