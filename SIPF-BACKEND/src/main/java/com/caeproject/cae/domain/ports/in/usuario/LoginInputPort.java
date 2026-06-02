package com.caeproject.cae.domain.ports.in.usuario;

import com.caeproject.cae.domain.ports.model.usuario.Usuario;

public interface LoginInputPort {
    Usuario loginSistema(String correo, String contrasena);
}
