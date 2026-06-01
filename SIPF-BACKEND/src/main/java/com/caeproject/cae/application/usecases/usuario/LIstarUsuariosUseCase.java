package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.in.usuario.LIstarUsuariosInputPort;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

import java.util.List;

public class LIstarUsuariosUseCase implements LIstarUsuariosInputPort {
    UsuarioRepository usuarioRepository;

    public LIstarUsuariosUseCase (UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }


    @Override
    public List<Usuario> listarUsuarios (){
        return usuarioRepository.findAll();
    }
}
