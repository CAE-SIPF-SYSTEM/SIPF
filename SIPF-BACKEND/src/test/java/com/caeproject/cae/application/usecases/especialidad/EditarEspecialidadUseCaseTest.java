package com.caeproject.cae.application.usecases.especialidad;

import com.caeproject.cae.application.usecases.especialidad.commands.EditarEspecialidadCommand;
import com.caeproject.cae.domain.ports.exceptions.especialidadexception.EspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.domain.ports.out.EspecialidadRepository;
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
class EditarEspecialidadUseCaseTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private EditarEspecialidadUseCase editarEspecialidadUseCase;

    private Especialidad especialidadExistente;
    private EditarEspecialidadCommand command;

    @BeforeEach
    void setUp() {
        especialidadExistente = new Especialidad();
        especialidadExistente.setId(1L);
        especialidadExistente.setNombreEspecialidad("PROGRAMACION BASICA");

        command = new EditarEspecialidadCommand();
        command.setNombreEspecialidad("PROGRAMACION AVANZADA");
    }

    @Test
    @DisplayName("Fase 1: Debe editar una especialidad existente correctamente")
    void editarEspecialidad_existente_debeActualizarExitosamente() {
        given(especialidadRepository.findById(1L)).willReturn(Optional.of(especialidadExistente));
        given(especialidadRepository.saveEspecialidad(any(Especialidad.class))).willReturn(especialidadExistente);

        Especialidad resultado = editarEspecialidadUseCase.editarEspecialidad(command, 1L);

        assertNotNull(resultado);
        assertEquals("PROGRAMACION AVANZADA", resultado.getNombreEspecialidad());
        verify(especialidadRepository).saveEspecialidad(especialidadExistente);
        System.out.println("[FASE 1 PASSED] Especialidad Editada -> ID: " + resultado.getId() + ", Nuevo Nombre: " + resultado.getNombreEspecialidad());
    }

    @Test
    @DisplayName("Fase 1: Debe lanzar EspecialidadNoEncontradaException si la ID no existe")
    void editarEspecialidad_inexistente_debeLanzarExcepcion() {
        given(especialidadRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(EspecialidadNoEncontradaException.class, () -> 
            editarEspecialidadUseCase.editarEspecialidad(command, 99L)
        );
        System.out.println("[FASE 1 PASSED] Excepción EspecialidadNoEncontradaException capturada correctamente al intentar editar ID 99L inexistente.");
    }
}
