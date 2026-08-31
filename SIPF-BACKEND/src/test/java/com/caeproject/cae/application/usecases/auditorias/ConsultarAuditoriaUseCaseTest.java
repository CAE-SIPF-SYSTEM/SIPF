package com.caeproject.cae.application.usecases.auditorias;

import com.caeproject.cae.domain.ports.model.AuditoriaPerfil;
import com.caeproject.cae.domain.ports.out.AuditoriaPerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConsultarAuditoriaUseCaseTest {

    @Mock
    private AuditoriaPerfilRepository auditoriaPerfilRepository;

    @InjectMocks
    private ConsultarAuditoriaUseCase consultarAuditoriaUseCase;

    private AuditoriaPerfil traza;

    @BeforeEach
    void setUp() {
        traza = new AuditoriaPerfil();
        traza.setId(1L);
        traza.setUsuarioId(2L);
        traza.setCampoModificado("ROLES");
        traza.setValorAnterior("INSTRUCTOR");
        traza.setValorNuevo("COORDINADOR");
        traza.setFechaModificacion(LocalDateTime.now());
    }

    @Test
    @DisplayName("Fase 0: Debe retornar lista vacía cuando no existen registros de auditoría")
    void listarTodas_sinDatos_debeRetornarListaVacia() {
        given(auditoriaPerfilRepository.listarTodas()).willReturn(Collections.emptyList());

        List<AuditoriaPerfil> resultado = consultarAuditoriaUseCase.listarTodas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        System.out.println(" [FASE 0 PASSED] Resultado sin registros: " + resultado);
    }

    @Test
    @DisplayName("Fase 1: Debe listar todas las trazas de auditoría cuando existen registros")
    void listarTodas_conDatos_debeRetornarLista() {
        given(auditoriaPerfilRepository.listarTodas()).willReturn(List.of(traza));

        List<AuditoriaPerfil> resultado = consultarAuditoriaUseCase.listarTodas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("ROLES", resultado.get(0).getCampoModificado());
        System.out.println(" [FASE 1 PASSED] Traza obtenida correctamente -> ID: " + resultado.get(0).getId() + ", Campo: " + resultado.get(0).getCampoModificado() + ", Nuevo Valor: " + resultado.get(0).getValorNuevo());
    }
}
