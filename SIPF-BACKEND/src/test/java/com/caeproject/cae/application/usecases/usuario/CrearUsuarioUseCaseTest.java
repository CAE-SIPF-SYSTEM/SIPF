package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.application.usecases.usuario.commands.CrearUsuarioCommand;
import com.caeproject.cae.domain.ports.exceptions.instructorexception.DocumentoYaRegistradoException;
import com.caeproject.cae.domain.ports.exceptions.sessionexceptions.ContrasenaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.CorreoYaRegistradoException;
import com.caeproject.cae.domain.ports.model.PerfilBase;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;
import com.caeproject.cae.domain.ports.out.PerfilBaseRepository;
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
class CrearUsuarioUseCaseTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PerfilBaseRepository perfilBaseRepository;

    @InjectMocks
    private CrearUsuarioUseCase crearUsuarioUseCase;

    private CrearUsuarioCommand command;
    private Usuario usuarioGuardado;

    @BeforeEach
    void setUp() {
        command = new CrearUsuarioCommand();
        command.setCorreo("nuevo@sipf.com");
        command.setContrasena("ClaveSegura123!");
        command.setRol(Rol.INSTRUCTOR);
        command.setNombre("Juan");
        command.setApellido("Perez");
        command.setDocumentoIdentidad(123456789L);
        command.setTelefono(3001234567L);
        command.setTipoContrato(TIpoContrato.CONTRATISTA);

        usuarioGuardado = new Usuario();
        usuarioGuardado.setId(10L);
        usuarioGuardado.setCorreo("nuevo@sipf.com");
        usuarioGuardado.setContrasena("ClaveSegura123!");
        usuarioGuardado.setRol(Rol.INSTRUCTOR);
        usuarioGuardado.setEstado(true);
    }

    @Test
    @DisplayName("Fase 1: Debe crear un usuario y su perfil base exitosamente")
    void crearUsuario_datosValidos_debeCrearUsuarioYPerfil() {
        given(usuarioRepository.findByCorreo("nuevo@sipf.com")).willReturn(Optional.empty());
        given(perfilBaseRepository.findByCC(123456789L)).willReturn(Optional.empty());
        given(usuarioRepository.saveUser(any(Usuario.class))).willReturn(usuarioGuardado);

        Usuario resultado = crearUsuarioUseCase.crearUsuario(command);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getId());
        assertEquals("nuevo@sipf.com", resultado.getCorreo());
        assertTrue(resultado.isEstado());
        verify(usuarioRepository).saveUser(any(Usuario.class));
        verify(perfilBaseRepository).save(any(PerfilBase.class));
        System.out.println("[FASE 1 PASSED] Usuario y PerfilBase creados -> ID: " + resultado.getId() + ", Correo: " + resultado.getCorreo() + ", CC: 123456789");
    }

    @Test
    @DisplayName("Fase 1: Debe lanzar CorreoYaRegistradoException si el correo ya existe")
    void crearUsuario_correoDuplicado_debeLanzarExcepcion() {
        given(usuarioRepository.findByCorreo("nuevo@sipf.com")).willReturn(Optional.of(usuarioGuardado));

        assertThrows(CorreoYaRegistradoException.class, () -> 
            crearUsuarioUseCase.crearUsuario(command)
        );
        System.out.println("[FASE 1 PASSED] CorreoYaRegistradoException capturada correctamente para correo duplicado.");
    }

    @Test
    @DisplayName("Fase 1: Debe lanzar DocumentoYaRegistradoException si la cedula ya existe")
    void crearUsuario_documentoDuplicado_debeLanzarExcepcion() {
        given(usuarioRepository.findByCorreo("nuevo@sipf.com")).willReturn(Optional.empty());
        given(perfilBaseRepository.findByCC(123456789L)).willReturn(Optional.of(new PerfilBase()));

        assertThrows(DocumentoYaRegistradoException.class, () -> 
            crearUsuarioUseCase.crearUsuario(command)
        );
        System.out.println("[FASE 1 PASSED] DocumentoYaRegistradoException capturada correctamente para cedula duplicada.");
    }

    @Test
    @DisplayName("Fase 1: Debe lanzar ContrasenaInvalidaException si la clave no cumple fortaleza")
    void crearUsuario_claveDebil_debeLanzarExcepcion() {
        command.setContrasena("debil");

        assertThrows(ContrasenaInvalidaException.class, () ->
            crearUsuarioUseCase.crearUsuario(command)
        );
        System.out.println("[FASE 1 PASSED] ContrasenaInvalidaException capturada correctamente al crear usuario con clave débil.");
    }
}
