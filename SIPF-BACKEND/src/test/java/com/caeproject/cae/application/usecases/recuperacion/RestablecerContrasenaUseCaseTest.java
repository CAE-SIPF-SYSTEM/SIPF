package com.caeproject.cae.application.usecases.recuperacion;

import com.caeproject.cae.domain.ports.exceptions.sessionexceptions.ContrasenaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.tokensexception.TokenExpiradoException;
import com.caeproject.cae.domain.ports.exceptions.tokensexception.TokenNoEncontradoException;
import com.caeproject.cae.domain.ports.model.TokenRecuperacion;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.out.TokenRecuperacionRepository;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RestablecerContrasenaUseCaseTest {

    @Mock
    private TokenRecuperacionRepository tokenRecuperacionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RestablecerContrasenaUseCase restablecerContrasenaUseCase;

    private TokenRecuperacion tokenValido;
    private TokenRecuperacion tokenExpirado;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        tokenValido = new TokenRecuperacion();
        tokenValido.setId(10L);
        tokenValido.setToken("valid-token-123");
        tokenValido.setUsuarioId(1L);
        tokenValido.setFechaExpiracion(LocalDateTime.now().plusMinutes(10));

        tokenExpirado = new TokenRecuperacion();
        tokenExpirado.setId(20L);
        tokenExpirado.setToken("expired-token-999");
        tokenExpirado.setUsuarioId(1L);
        tokenExpirado.setFechaExpiracion(LocalDateTime.now().minusMinutes(5));

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("test@sipf.com");
        usuario.setContrasena("OldPassword123!");
    }

    @Test
    @DisplayName("Fase 1: Debe restablecer la contraseña exitosamente cuando el token es válido y la clave cumple política")
    void restablecerContrasena_tokenValido_debeActualizarClave() {
        given(tokenRecuperacionRepository.buscarPorToken("valid-token-123")).willReturn(Optional.of(tokenValido));
        given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario));

        restablecerContrasenaUseCase.restablecerContrasena("valid-token-123", "NuevaClaveSegura123!");

        assertEquals("NuevaClaveSegura123!", usuario.getContrasena());
        verify(usuarioRepository).saveUser(usuario);
        verify(tokenRecuperacionRepository).eliminar(10L);
        System.out.println("[FASE 1 PASSED] Contraseña restablecida con éxito para el usuario ID: 1L");
    }

    @Test
    @DisplayName("Fase 1: Debe lanzar ContrasenaInvalidaException si la nueva clave no cumple los requisitos de fortaleza")
    void restablecerContrasena_claveDebil_debeLanzarExcepcion() {
        assertThrows(ContrasenaInvalidaException.class, () -> 
            restablecerContrasenaUseCase.restablecerContrasena("valid-token-123", "debil")
        );
        System.out.println("[FASE 1 PASSED] ContrasenaInvalidaException capturada correctamente para clave débil.");
    }

    @Test
    @DisplayName("Fase 0: Debe lanzar TokenNoEncontradoException si el token no existe")
    void restablecerContrasena_tokenInexistente_debeLanzarExcepcion() {
        given(tokenRecuperacionRepository.buscarPorToken("fake-token")).willReturn(Optional.empty());

        assertThrows(TokenNoEncontradoException.class, () -> 
            restablecerContrasenaUseCase.restablecerContrasena("fake-token", "NuevaClaveSegura123!")
        );
        System.out.println("[FASE 0 PASSED] TokenNoEncontradoException capturada correctamente para token inexistente.");
    }

    @Test
    @DisplayName("Fase 1: Debe lanzar TokenExpiradoException si el token ya venció")
    void restablecerContrasena_tokenExpirado_debeLanzarExcepcion() {
        given(tokenRecuperacionRepository.buscarPorToken("expired-token-999")).willReturn(Optional.of(tokenExpirado));

        assertThrows(TokenExpiradoException.class, () -> 
            restablecerContrasenaUseCase.restablecerContrasena("expired-token-999", "NuevaClaveSegura123!")
        );
        verify(tokenRecuperacionRepository).eliminar(20L);
        System.out.println("[FASE 1 PASSED] TokenExpiradoException capturada correctamente y token expirado eliminado de BD.");
    }
}
