package com.caeproject.cae.application.usecases.especialidad;

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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EliminarEspecialidadUseCaseTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private EliminarEspecialidadUseCase eliminarEspecialidadUseCase;

    private Especialidad especialidad;

    @BeforeEach
    void setUp() {
        especialidad = new Especialidad();
        especialidad.setId(1L);
        especialidad.setNombreEspecialidad("DISEÑO GRAFICO");
    }

    @Test
    @DisplayName("Fase 1: Debe eliminar una especialidad existente correctamente")
    void eliminarEspecialidad_existente_debeEliminarExitosamente() {
        given(especialidadRepository.findById(1L)).willReturn(Optional.of(especialidad));

        eliminarEspecialidadUseCase.eliminarEspecialidad(1L);

        verify(especialidadRepository).eliminarEspecialidad(1L);
        System.out.println("[FASE 1 PASSED] Especialidad eliminada correctamente en el repositorio -> ID: 1L (" + especialidad.getNombreEspecialidad() + ")");
    }

    @Test
    @DisplayName("Fase 1: Debe lanzar EspecialidadNoEncontradaException al intentar eliminar una ID no existente")
    void eliminarEspecialidad_inexistente_debeLanzarExcepcion() {
        given(especialidadRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(EspecialidadNoEncontradaException.class, () -> 
            eliminarEspecialidadUseCase.eliminarEspecialidad(99L)
        );
        System.out.println("[FASE 1 PASSED] Excepción EspecialidadNoEncontradaException capturada correctamente al intentar eliminar ID 99L inexistente.");
    }
}
