package com.caeproject.cae.application.usecases.asignacioninstructor;

import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;
import com.caeproject.cae.domain.ports.out.*;
import com.caeproject.cae.domain.ports.service.ValidarElegibilidadInstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SugerirInstructorUseCaseTest {

    @Mock
    private PerfilBaseRepository perfilBaseRepository;
    @Mock
    private ValidarElegibilidadInstructor validarElegibilidadInstructor;
    @Mock
    private DisponibilidadInstructorRepository disponibilidadInstructorRepository;
    @Mock
    private InstructorEspecialidadRepository instructorEspecialidadRepository;
    @Mock
    private CompetenciaEspecialidadRepository competenciaEspecialidadRepository;
    @Mock
    private ProgramaRepository programaRepository;
    @Mock
    private FichaRepository fichaRepository;

    @InjectMocks
    private SugerirInstructorUseCase sugerirInstructorUseCase;

    private DisponibilidadInstructor instructorPlanta;
    private DisponibilidadInstructor instructorContratista;
    private PerfilBase perfilPlanta;
    private PerfilBase perfilContratista;
    private Ficha ficha;
    private Programa programa;
    private CompetenciaEspecialidad competenciaEspecialidad;

    @BeforeEach
    void setUp() {
        instructorPlanta = new DisponibilidadInstructor();
        instructorPlanta.setUsuarioId(1L);

        perfilPlanta = new PerfilBase();
        perfilPlanta.setUsuarioId(1L);
        perfilPlanta.setTipoContrato(TIpoContrato.PLANTA);

        instructorContratista = new DisponibilidadInstructor();
        instructorContratista.setUsuarioId(2L);

        perfilContratista = new PerfilBase();
        perfilContratista.setUsuarioId(2L);
        perfilContratista.setTipoContrato(TIpoContrato.CONTRATISTA);

        ficha = new Ficha();
        ficha.setId(10L);
        ficha.setProgramaId(100L);

        programa = new Programa();
        programa.setId(100L);

        competenciaEspecialidad = new CompetenciaEspecialidad();
    }

    @Test
    void debeSugerirInstructoresOrdenandoPrimeroLosDePlanta() {
        // Arrange
        Long competenciaId = 5L;
        Long fichaId = 10L;
        Long horasRequeridas = 10L;

        when(competenciaEspecialidadRepository.findByCompetenciaId(competenciaId))
                .thenReturn(Optional.of(competenciaEspecialidad));
        
        when(fichaRepository.findById(fichaId))
                .thenReturn(Optional.of(ficha));
        
        when(programaRepository.findById(100L))
                .thenReturn(Optional.of(programa));

        List<DisponibilidadInstructor> instructoresMock = new java.util.ArrayList<>();
        System.out.println("----- PRUEBA: debeSugerirInstructoresOrdenandoPrimeroLosDePlanta (30 Instructores) -----");
        System.out.println("Generando 30 instructores alternados...");

        for (int i = 1; i <= 30; i++) {
            Long id = (long) i + 100; // start from 101 to avoid conflict with existing setups
            // Aleatorizar un poco o intercalar (pares contratista, impares planta)
            TIpoContrato tipo = (i % 2 == 0) ? TIpoContrato.CONTRATISTA : TIpoContrato.PLANTA;

            DisponibilidadInstructor inst = new DisponibilidadInstructor();
            inst.setUsuarioId(id);
            instructoresMock.add(inst);

            PerfilBase pb = new PerfilBase();
            pb.setUsuarioId(id);
            pb.setTipoContrato(tipo);

            when(perfilBaseRepository.findById(id)).thenReturn(Optional.of(pb));
            when(instructorEspecialidadRepository.findByInstructorId(id)).thenReturn(Optional.empty());
            when(validarElegibilidadInstructor.esElegible(
                    eq(inst), any(), eq(competenciaEspecialidad), eq(programa), eq(horasRequeridas), eq(true)
            )).thenReturn(true);
        }

        // Desordenar la lista intencionalmente para la prueba
        Collections.shuffle(instructoresMock);
        
        when(disponibilidadInstructorRepository.findAll()).thenReturn(instructoresMock);

        System.out.println("Datos que el repositorio entregará (Desordenados):");
        for (int i = 0; i < instructoresMock.size(); i++) {
            Long id = instructoresMock.get(i).getUsuarioId();
            String tipo = (id % 2 != 0) ? "PLANTA" : "CONTRATISTA";
            System.out.println((i + 1) + ". Instructor ID: " + id + " | Tipo Contrato: " + tipo);
        }

        List<DisponibilidadInstructor> result = sugerirInstructorUseCase.sugerirInstructores(competenciaId, fichaId, horasRequeridas);

        System.out.println("\nResultado después del filtro y ordenamiento por el UseCase:");
        int plantaCount = 0;
        int contratistaCount = 0;
        for (int i = 0; i < result.size(); i++) {
            Long uid = result.get(i).getUsuarioId();
            String tipo = (uid % 2 != 0) ? "PLANTA" : "CONTRATISTA";
            System.out.println((i + 1) + ". Instructor ID: " + uid + " | Tipo Contrato: " + tipo);
            
            if (tipo.equals("PLANTA")) {
                plantaCount++;
                // Todos los de planta deben ir primero, así que si ya vimos un contratista, esto falla
                assertEquals(0, contratistaCount, "Se encontró un PLANTA después de un CONTRATISTA");
            } else {
                contratistaCount++;
            }
        }
        System.out.println("----------------------------------------------------------------------\n");

        assertEquals(30, result.size());
        assertEquals(15, plantaCount);
        assertEquals(15, contratistaCount);
    }

    @Test
    void debeBuscarInstructoresConFiltroSecundarioSiElPrincipalNoEncuentra() {
        // Arrange
        Long competenciaId = 5L;
        Long fichaId = 10L;
        Long horasRequeridas = 10L;

        when(competenciaEspecialidadRepository.findByCompetenciaId(competenciaId)).thenReturn(Optional.of(competenciaEspecialidad));
        when(fichaRepository.findById(fichaId)).thenReturn(Optional.of(ficha));
        when(programaRepository.findById(100L)).thenReturn(Optional.of(programa));

        when(disponibilidadInstructorRepository.findAll())
                .thenReturn(Collections.singletonList(instructorPlanta));

        when(perfilBaseRepository.findById(1L)).thenReturn(Optional.of(perfilPlanta));

        when(instructorEspecialidadRepository.findByInstructorId(1L))
                .thenReturn(Optional.empty());

        // Falla en filtro estricto (true)
        when(validarElegibilidadInstructor.esElegible(
                eq(instructorPlanta), any(), eq(competenciaEspecialidad), eq(programa), eq(horasRequeridas), eq(true)
        )).thenReturn(false);

        // Pasa en filtro menos estricto (false)
        when(validarElegibilidadInstructor.esElegible(
                eq(instructorPlanta), any(), eq(competenciaEspecialidad), eq(programa), eq(horasRequeridas), eq(false)
        )).thenReturn(true);

        // Act
        List<DisponibilidadInstructor> result = sugerirInstructorUseCase.sugerirInstructores(competenciaId, fichaId, horasRequeridas);

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getUsuarioId());
    }
}
