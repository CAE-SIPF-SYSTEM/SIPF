package com.caeproject.cae.domain.ports.in.usuario;

import com.caeproject.cae.domain.ports.model.perfil_base.PerfilBase;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;

public interface CrearUsuarioInputPort {
    Usuario crearUsuario (Usuario usuario, PerfilBase perfilBase);

}
