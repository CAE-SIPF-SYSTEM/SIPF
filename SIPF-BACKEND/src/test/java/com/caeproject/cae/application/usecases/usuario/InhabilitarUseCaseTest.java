package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InhabilitarUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private InhabilitarUseCase inhabilitarUseCase;

    private Usuario usuarioActivo;

    @BeforeEach
    void setUp() {
        usuarioActivo = new Usuario();
        usuarioActivo.setId(5L);
        usuarioActivo.setCorreo("activo@sipf.com");
        usuarioActivo.setEstado(true);
    }

    @Test
    @DisplayName("Fase 1: Debe inhabilitar un usuario activo cambiando su estado a false")
    void inhabilitarUsuario_existente_debeCambiarEstadoAFalse() {
        given(usuarioRepository.findById(5L)).willReturn(Optional.of(usuarioActivo));
        given(usuarioRepository.saveUser(any(Usuario.class))).willReturn(usuarioActivo);

        Usuario resultado = inhabilitarUseCase.inhabilitarUsuario(5L);

        assertNotNull(resultado);
        assertFalse(resultado.isEstado());
        verify(usuarioRepository).saveUser(usuarioActivo);
        System.out.println(" [FASE 1 PASSED] Usuario ID 5L inhabilitado correctamente. Estado actual: " + resultado.isEstado());
    }

    @Test
    @DisplayName("Fase 0/1: Debe lanzar UsuarioNoEncontradoException si el ID no existe")
    void inhabilitarUsuario_inexistente_debeLanzarExcepcion() {
        given(usuarioRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> 
            inhabilitarUseCase.inhabilitarUsuario(99L)
        );
        System.out.println("[FASE 0 PASSED] UsuarioNoEncontradoException capturada correctamente para ID inexistente.");
    }
}
