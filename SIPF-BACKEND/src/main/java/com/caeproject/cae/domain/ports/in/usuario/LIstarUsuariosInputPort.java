package com.caeproject.cae.domain.ports.in.usuario;

import com.caeproject.cae.domain.ports.model.Usuario;

import java.util.List;

public interface LIstarUsuariosInputPort {
    List<Usuario> listarUsuarios();
}
