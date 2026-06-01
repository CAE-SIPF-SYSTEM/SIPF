package com.caeproject.cae.domain.ports.in.usuario;

import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;

import java.util.List;
import java.util.Optional;

public interface ObtenerUsuarioInputPort {
    Usuario obtenerUsuario (Long id);
    Optional<Usuario> porCorreo(String correo);
    List<Usuario> porRol(Rol rol);
}
