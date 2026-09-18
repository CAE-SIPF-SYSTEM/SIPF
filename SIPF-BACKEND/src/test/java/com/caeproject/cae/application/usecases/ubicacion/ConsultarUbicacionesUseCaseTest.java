package com.caeproject.cae.application.usecases.ubicacion;

import com.caeproject.cae.domain.ports.model.Departamento;
import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.out.UbicacionRepository;
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
class ConsultarUbicacionesUseCaseTest {

    @Mock
    private UbicacionRepository ubicacionRepository;

    @InjectMocks
    private ConsultarUbicacionesUseCase consultarUbicacionesUseCase;

    private Departamento departamento;
    private Municipio municipio;

    @BeforeEach
    void setUp() {
        departamento = new Departamento();
        departamento.setId(1L);
        departamento.setNombre("ANTIOQUIA");

        municipio = new Municipio();
        municipio.setId(101L);
        municipio.setNombre("MEDELLIN");
        municipio.setDepartamento(departamento);
    }

    @Test
    @DisplayName("Fase 0: Debe retornar lista vacía de departamentos si no existen registros")
    void obtenerTodosLosDepartamentos_sinDatos_debeRetornarListaVacia() {
        given(ubicacionRepository.obtenerDepartamentos()).willReturn(Collections.emptyList());

        List<Departamento> resultado = consultarUbicacionesUseCase.obtenerTodosLosDepartamentos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        System.out.println("[FASE 0 PASSED] Departamentos en BD vacía: " + resultado);
    }

    @Test
    @DisplayName("Fase 1: Debe retornar la lista de departamentos cuando existen registros")
    void obtenerTodosLosDepartamentos_conDatos_debeRetornarLista() {
        given(ubicacionRepository.obtenerDepartamentos()).willReturn(List.of(departamento));

        List<Departamento> resultado = consultarUbicacionesUseCase.obtenerTodosLosDepartamentos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ANTIOQUIA", resultado.get(0).getNombre());
        System.out.println("[FASE 1 PASSED] Departamento retornado -> ID: " + resultado.get(0).getId() + ", Nombre: " + resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("Fase 1: Debe retornar un municipio por su ID si existe")
    void obtenerMunicipioPorId_existente_debeRetornarOptionalConMunicipio() {
        given(ubicacionRepository.obtenerMunicipioPorId(101L)).willReturn(Optional.of(municipio));

        Optional<Municipio> resultado = consultarUbicacionesUseCase.obtenerMunicipioPorId(101L);

        assertTrue(resultado.isPresent());
        assertEquals("MEDELLIN", resultado.get().getNombre());
        System.out.println("[FASE 1 PASSED] Municipio encontrado -> ID: " + resultado.get().getId() + ", Nombre: " + resultado.get().getNombre() + ", Dpto: " + resultado.get().getDepartamento().getNombre());
    }
}
