package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.CredencialesIncorrectasException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioInhabilitadoException;
import com.caeproject.cae.domain.ports.in.usuario.LoginInputPort;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import com.caeproject.cae.infraestructure.security.JwtUtil; // Asegura que este import esté correcto

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginUseCase implements LoginInputPort {

    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    public LoginUseCase(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Usuario loginSistema(String correo, String contrasena) { // Quité 'tokenSession' de los parámetros porque el frontend no lo envía, nosotros lo creamos

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new CredencialesIncorrectasException());

        if (!usuario.isEstado()) {
            throw new UsuarioInhabilitadoException(usuario.getId());
        }

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new CredencialesIncorrectasException();
        }
        
        String nuevoTokenSession = UUID.randomUUID().toString();

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", usuario.getRol().name());
        extraClaims.put("sessionId", nuevoTokenSession);

        String jwtToken = jwtUtil.generateToken(extraClaims, usuario.getCorreo());

        usuario.setTokenSession(nuevoTokenSession);
        usuario.setJwtToken(jwtToken);
        usuarioRepository.saveUser(usuario);

        return usuario;
    }
}