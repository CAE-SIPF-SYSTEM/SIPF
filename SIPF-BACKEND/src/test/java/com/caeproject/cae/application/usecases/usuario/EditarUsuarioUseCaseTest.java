package com.caeproject.cae.application.usecases.usuario;

import com.caeproject.cae.application.usecases.usuario.commands.EditarUsuarioCommand;
import com.caeproject.cae.domain.ports.model.AuditoriaPerfil;
import com.caeproject.cae.domain.ports.model.PerfilBase;
import com.caeproject.cae.domain.ports.model.Usuario;
import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.out.AuditoriaPerfilRepository;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditarUsuarioUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private AuditoriaPerfilRepository auditoriaPerfilRepository;
    private EditarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        auditoriaPerfilRepository = mock(AuditoriaPerfilRepository.class);
        useCase = new EditarUsuarioUseCase(usuarioRepository, auditoriaPerfilRepository);
    }

    @Test
    void debeRegistrarAuditoriaAlEditarNombreYCorreo() {
        // Arrange
        Long id = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(id);
        usuarioExistente.setCorreo("viejo@correo.com");
        usuarioExistente.setRol(Rol.INSTRUCTOR);

        PerfilBase perfil = new PerfilBase();
        perfil.setNombre("NombreViejo");
        usuarioExistente.setPerfilBase(perfil);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.saveUser(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        EditarUsuarioCommand command = new EditarUsuarioCommand();
        command.setCorreo("nuevo@correo.com");
        command.setNombre("NombreNuevo");

        // Act
        useCase.editarUsuario(command, id);

        // Assert
        ArgumentCaptor<AuditoriaPerfil> captor = ArgumentCaptor.forClass(AuditoriaPerfil.class);
        verify(auditoriaPerfilRepository, times(2)).guardarAuditoria(captor.capture());

        AuditoriaPerfil audCorreo = captor.getAllValues().stream()
                .filter(a -> a.getCampoModificado().equals("correo"))
                .findFirst().orElse(null);

        assertNotNull(audCorreo);
        assertEquals("viejo@correo.com", audCorreo.getValorAnterior());
        assertEquals("nuevo@correo.com", audCorreo.getValorNuevo());

        AuditoriaPerfil audNombre = captor.getAllValues().stream()
                .filter(a -> a.getCampoModificado().equals("nombre"))
                .findFirst().orElse(null);

        assertNotNull(audNombre);
        assertEquals("NombreViejo", audNombre.getValorAnterior());
        assertEquals("NombreNuevo", audNombre.getValorNuevo());
    }
}
