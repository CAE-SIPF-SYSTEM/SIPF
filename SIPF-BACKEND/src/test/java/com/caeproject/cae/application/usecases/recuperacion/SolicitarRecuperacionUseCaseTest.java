package com.caeproject.cae.application.usecases.recuperacion;

import com.caeproject.cae.domain.ports.model.TokenRecuperacion;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.out.EmailNotificationPort;
import com.caeproject.cae.domain.ports.out.TokenRecuperacionRepository;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitarRecuperacionUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TokenRecuperacionRepository tokenRecuperacionRepository;

    @Mock
    private EmailNotificationPort emailNotificationPort;

    @InjectMocks
    private SolicitarRecuperacionUseCase solicitarRecuperacionUseCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("recuperar@sipf.com");
    }

    @Test
    @DisplayName("Fase 1: Debe generar token y enviar correo cuando el usuario existe")
    void solicitarRecuperacion_usuarioExistente_debeGenerarTokenYEnviarCorreo() {
        given(usuarioRepository.findByCorreo("recuperar@sipf.com")).willReturn(Optional.of(usuario));

        solicitarRecuperacionUseCase.solicitarRecuperacion("recuperar@sipf.com");

        verify(tokenRecuperacionRepository).eliminarPorUsuarioId(1L);
        verify(tokenRecuperacionRepository).guardar(any(TokenRecuperacion.class));
        verify(emailNotificationPort).enviarEmailRecuperacion(eq("recuperar@sipf.com"), anyString());
        System.out.println("[FASE 1 PASSED] Solicitar Recuperación -> Token generado y enviado a recuperar@sipf.com");
    }

    @Test
    @DisplayName("Fase 0: No debe hacer nada ni lanzar excepción si el correo no existe en el sistema")
    void solicitarRecuperacion_usuarioInexistente_debeManejarSilenciosamente() {
        given(usuarioRepository.findByCorreo("noexiste@sipf.com")).willReturn(Optional.empty());

        solicitarRecuperacionUseCase.solicitarRecuperacion("noexiste@sipf.com");

        verify(tokenRecuperacionRepository, never()).guardar(any());
        verify(emailNotificationPort, never()).enviarEmailRecuperacion(anyString(), anyString());
        System.out.println("[FASE 0 PASSED] Correo inexistente manejado silenciosamente sin exponer información.");
    }
}
