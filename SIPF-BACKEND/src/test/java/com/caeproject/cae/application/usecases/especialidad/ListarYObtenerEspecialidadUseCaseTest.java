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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ListarYObtenerEspecialidadUseCaseTest {

    @Mock
    private EspecialidadRepository especialidadRepository;

    @InjectMocks
    private ListarEspecialidadUseCase listarEspecialidadUseCase;

    @InjectMocks
    private ObtenerEspecialidadUseCase obtenerEspecialidadUseCase;

    private Especialidad especialidad;

    @BeforeEach
    void setUp() {
        especialidad = new Especialidad();
        especialidad.setId(1L);
        especialidad.setNombreEspecialidad("REDES Y TELECOMUNICACIONES");
    }

    @Test
    @DisplayName("Fase 0: Debe retornar lista vacía si no hay especialidades registradas")
    void listarEspecialidades_sinDatos_debeRetornarListaVacia() {
        given(especialidadRepository.findAll()).willReturn(Collections.emptyList());

        List<Especialidad> resultado = listarEspecialidadUseCase.listarEspecialidades();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        System.out.println("[FASE 0 PASSED] Lista vacía de especialidades obtenida: " + resultado);
    }

    @Test
    @DisplayName("Fase 1: Debe listar todas las especialidades cuando existen registros")
    void listarEspecialidades_conDatos_debeRetornarLista() {
        given(especialidadRepository.findAll()).willReturn(List.of(especialidad));

        List<Especialidad> resultado = listarEspecialidadUseCase.listarEspecialidades();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("REDES Y TELECOMUNICACIONES", resultado.get(0).getNombreEspecialidad());
        System.out.println("[FASE 1 PASSED] Especialidad en lista -> ID: " + resultado.get(0).getId() + ", Nombre: " + resultado.get(0).getNombreEspecialidad());
    }

    @Test
    @DisplayName("Fase 1: Debe obtener una especialidad por ID existente")
    void obtenerEspecialidad_existente_debeRetornarEspecialidad() {
        given(especialidadRepository.findById(1L)).willReturn(Optional.of(especialidad));

        Especialidad resultado = obtenerEspecialidadUseCase.obtenerEspecialidad(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("REDES Y TELECOMUNICACIONES", resultado.getNombreEspecialidad());
        System.out.println("[FASE 1 PASSED] Especialidad consultada por ID -> ID: " + resultado.getId() + ", Nombre: " + resultado.getNombreEspecialidad());
    }

    @Test
    @DisplayName("Fase 0/1: Debe lanzar EspecialidadNoEncontradaException si la ID no existe")
    void obtenerEspecialidad_inexistente_debeLanzarExcepcion() {
        given(especialidadRepository.findById(99L)).willReturn(Optional.empty());

        assertThrows(EspecialidadNoEncontradaException.class, () -> 
            obtenerEspecialidadUseCase.obtenerEspecialidad(99L)
        );
        System.out.println("[FASE 1 PASSED] Excepción EspecialidadNoEncontradaException capturada correctamente al consultar ID 99L inexistente.");
    }
}
