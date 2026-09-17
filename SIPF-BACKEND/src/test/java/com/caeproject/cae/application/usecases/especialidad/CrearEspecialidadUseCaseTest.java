package com.caeproject.cae.application.usecases.especialidad;

import com.caeproject.cae.application.usecases.especialidad.commands.CrearEspecialidadCommand;
import com.caeproject.cae.domain.ports.exceptions.especialidadexception.EspecialidadEnUsoException;
import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.domain.ports.out.EspecialidadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CrearEspecialidadUseCaseTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private CrearEspecialidadUseCase crearEspecialidadUseCase;

    private CrearEspecialidadCommand command;
    private Especialidad especialidadGuardada;

    @BeforeEach
    void setUp() {
        command = new CrearEspecialidadCommand();
        command.setNombreEspecialidad("DESARROLLO DE SOFTWARE");

        especialidadGuardada = new Especialidad();
        especialidadGuardada.setId(1L);
        especialidadGuardada.setNombreEspecialidad("DESARROLLO DE SOFTWARE");
    }

    @Test
    @DisplayName("Fase 1: Debe crear una especialidad exitosamente cuando el nombre no existe")
    void crearEspecialidad_nombreNuevo_debeCrearExitosamente() {
        given(especialidadRepository.existByNombre("DESARROLLO DE SOFTWARE")).willReturn(false);
        given(especialidadRepository.saveEspecialidad(any(Especialidad.class))).willReturn(especialidadGuardada);

        Especialidad resultado = crearEspecialidadUseCase.crearEspecialidad(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("DESARROLLO DE SOFTWARE", resultado.getNombreEspecialidad());
        verify(especialidadRepository).saveEspecialidad(any(Especialidad.class));
        System.out.println("[FASE 1 PASSED] Especialidad Creada -> ID: " + resultado.getId() + ", Nombre: " + resultado.getNombreEspecialidad());
    }

    @Test
    @DisplayName("Fase 1: Debe lanzar EspecialidadEnUsoException cuando el nombre ya existe")
    void crearEspecialidad_nombreExistente_debeLanzarExcepcion() {
        given(especialidadRepository.existByNombre("DESARROLLO DE SOFTWARE")).willReturn(true);

        assertThrows(EspecialidadEnUsoException.class, () -> 
            crearEspecialidadUseCase.crearEspecialidad(command)
        );
        System.out.println("[FASE 1 PASSED] Excepción EspecialidadEnUsoException capturada correctamente al intentar crear un duplicado.");
    }
}
