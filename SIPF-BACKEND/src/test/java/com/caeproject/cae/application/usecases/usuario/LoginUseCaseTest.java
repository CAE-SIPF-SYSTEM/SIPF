package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.CredencialesIncorrectasException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioInhabilitadoException;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import com.caeproject.cae.infraestructure.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private Usuario usuarioActivo;
    private Usuario usuarioInactivo;

    @BeforeEach
    void setUp() {
        usuarioActivo = new Usuario();
        usuarioActivo.setId(1L);
        usuarioActivo.setCorreo("admin@sipf.com");
        usuarioActivo.setContrasena("Password123!");
        usuarioActivo.setEstado(true);
        usuarioActivo.setRol(Rol.ADMINISTRADOR);

        usuarioInactivo = new Usuario();
        usuarioInactivo.setId(2L);
        usuarioInactivo.setCorreo("desactivado@sipf.com");
        usuarioInactivo.setContrasena("Password123!");
        usuarioInactivo.setEstado(false);
        usuarioInactivo.setRol(Rol.INSTRUCTOR);
    }

    @Test
    @DisplayName("Fase 1: Debe iniciar sesión exitosamente con credenciales válidas y generar JWT")
    void loginSistema_credencialesValidas_debeRetornarUsuarioConToken() {
        given(usuarioRepository.findByCorreo("admin@sipf.com")).willReturn(Optional.of(usuarioActivo));
        given(jwtUtil.generateToken(anyMap(), eq("admin@sipf.com"))).willReturn("mocked-jwt-token-12345");

        Usuario resultado = loginUseCase.loginSistema("admin@sipf.com", "Password123!");

        assertNotNull(resultado);
        assertEquals("admin@sipf.com", resultado.getCorreo());
        assertNotNull(resultado.getJwtToken());
        assertEquals("mocked-jwt-token-12345", resultado.getJwtToken());
        verify(usuarioRepository).saveUser(usuarioActivo);
        System.out.println("[FASE 1 PASSED] Login Exitoso -> Usuario: " + resultado.getCorreo() + ", Rol: " + resultado.getRol() + ", JWT Generado: " + resultado.getJwtToken());
    }

    @Test
    @DisplayName("Fase 0/1: Debe lanzar CredencialesIncorrectasException si el correo no existe")
    void loginSistema_correoInexistente_debeLanzarExcepcion() {
        given(usuarioRepository.findByCorreo("inexistente@sipf.com")).willReturn(Optional.empty());

        assertThrows(CredencialesIncorrectasException.class, () -> 
            loginUseCase.loginSistema("inexistente@sipf.com", "Password123!")
        );
        System.out.println(" [FASE 0 PASSED] CredencialesIncorrectasException capturada correctamente para correo inexistente.");
    }

    @Test
    @DisplayName("Fase 1: Debe lanzar CredencialesIncorrectasException si la contraseña es incorrecta")
    void loginSistema_contrasenaIncorrecta_debeLanzarExcepcion() {
        given(usuarioRepository.findByCorreo("admin@sipf.com")).willReturn(Optional.of(usuarioActivo));

        assertThrows(CredencialesIncorrectasException.class, () -> 
            loginUseCase.loginSistema("admin@sipf.com", "ClaveErronea123")
        );
        System.out.println("[FASE 1 PASSED] CredencialesIncorrectasException capturada correctamente para clave incorrecta.");
    }

    @Test
    @DisplayName("Fase 1: Debe lanzar UsuarioInhabilitadoException si el usuario está inactivo")
    void loginSistema_usuarioInhabilitado_debeLanzarExcepcion() {
        given(usuarioRepository.findByCorreo("desactivado@sipf.com")).willReturn(Optional.of(usuarioInactivo));

        assertThrows(UsuarioInhabilitadoException.class, () -> 
            loginUseCase.loginSistema("desactivado@sipf.com", "Password123!")
        );
        System.out.println("[FASE 1 PASSED] UsuarioInhabilitadoException capturada correctamente para usuario inhabilitado.");
    }
}
