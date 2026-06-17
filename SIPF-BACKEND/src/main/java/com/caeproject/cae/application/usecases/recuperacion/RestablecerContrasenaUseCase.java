package com.caeproject.cae.application.usecases.recuperacion;

import com.caeproject.cae.domain.ports.exceptions.tokensException.TokenExpiradoException;
import com.caeproject.cae.domain.ports.exceptions.tokensException.TokenNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.usuarioExceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.recuperacion.RestablecerContrasenaInputPort;
import com.caeproject.cae.domain.ports.model.token.TokenRecuperacion;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.TokenRecuperacionRepository;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;

import com.caeproject.cae.application.utils.ValidacionContrasena;
import com.caeproject.cae.domain.ports.exceptions.sessionExceptions.ContrasenaInvalidaException;

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
        if (!ValidacionContrasena.esValida(nuevaContrasena)) {
            throw new ContrasenaInvalidaException("La contraseña debe tener mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula, 1 número y 1 símbolo.");
        }
        
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
