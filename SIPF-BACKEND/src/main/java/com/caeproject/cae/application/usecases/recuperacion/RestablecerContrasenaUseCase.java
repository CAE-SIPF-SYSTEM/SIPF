package com.caeproject.cae.application.usecases.recuperacion;

import com.caeproject.cae.domain.ports.exceptions.TokenExpiradoException;
import com.caeproject.cae.domain.ports.exceptions.TokenNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.recuperacion.RestablecerContrasenaInputPort;
import com.caeproject.cae.domain.ports.model.token.TokenRecuperacion;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.TokenRecuperacionRepository;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

public class RestablecerContrasenaUseCase implements RestablecerContrasenaInputPort {

    private final TokenRecuperacionRepository tokenRecuperacionRepository;
    private final UsuarioRepository usuarioRepository;

    public RestablecerContrasenaUseCase(TokenRecuperacionRepository tokenRecuperacionRepository,
                                         UsuarioRepository usuarioRepository) {
        this.tokenRecuperacionRepository = tokenRecuperacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void restablecerContrasena(String token, String nuevaContrasena) {
        // Buscar el token
        TokenRecuperacion tokenRecuperacion = tokenRecuperacionRepository
                .buscarPorToken(token)
                .orElseThrow(TokenNoEncontradoException::new);

        // Verificacion de vencimiento de token
        if (tokenRecuperacion.estaExpirado()) {
            // Elimina token que ya expiro
            tokenRecuperacionRepository.eliminar(tokenRecuperacion.getId());
            throw new TokenExpiradoException();
        }

        // Busqueda de usuario asociado al token
        Usuario usuario = usuarioRepository
                .findById(tokenRecuperacion.getUsuarioId())
                .orElseThrow(() -> new UsuarioNoEncontradoException(tokenRecuperacion.getUsuarioId()));

        // actualizacion de contraseña
        usuario.setContrasena(nuevaContrasena);
        usuarioRepository.saveUser(usuario);

        // Eliminacion de token usado
        tokenRecuperacionRepository.eliminar(tokenRecuperacion.getId());
    }
}
