package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.CredencialesIncorrectasException;
import com.caeproject.cae.domain.ports.exceptions.UsuarioInhabilitadoException;
import com.caeproject.cae.domain.ports.in.usuario.LoginInputPort;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

public class LoginUseCase implements LoginInputPort {

    private final UsuarioRepository usuarioRepository;

    public LoginUseCase (UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario loginSistema(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(()-> new CredencialesIncorrectasException());
    if(!usuario.isEstado()){
        throw new UsuarioInhabilitadoException(usuario.getId());
    }

    if (!usuario.getContrasena().equals(contrasena)){
        throw new CredencialesIncorrectasException();
    }

    return usuario;
    }

}
