package com.caeproject.cae.application.usecases.competenciaespecialidad;

import com.caeproject.cae.application.usecases.competenciaespecialidad.commands.AsignarEspecialidadCompetenciaCommand;
import com.caeproject.cae.application.usecases.competenciaespecialidad.commands.EditarEspecialidadCompetenciaCommand;
import com.caeproject.cae.domain.ports.exceptions.competenciaespecialidadexception.CompetenciaEspecialidadDuplicadaException;
import com.caeproject.cae.domain.ports.exceptions.competenciaespecialidadexception.CompetenciaEspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;
import com.caeproject.cae.domain.ports.out.CompetenciaEspecialidadRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class CompetenciaEspecialidadUseCasesTest {

    @Mock
    private CompetenciaEspecialidadRepository competenciaEspecialidadRepository;

    @InjectMocks
    private AsignarEspecialidadCompetenciaUseCase asignarEspecialidadCompetenciaUseCase;

    @InjectMocks
    private DesasignarEspecialidadCompetenciaUseCase desasignarEspecialidadCompetenciaUseCase;

    @InjectMocks
    private EditarEspecialidadCompetenciaUseCase editarEspecialidadCompetenciaUseCase;

    @InjectMocks
    private ListarEspecialidadesCompetenciaUseCase listarEspecialidadesCompetenciaUseCase;

    @Test
    @DisplayName("Asignar especialidad a competencia duplicada")
    void asignar_especialidad_competencia_duplicada() {
        AsignarEspecialidadCompetenciaCommand command = new AsignarEspecialidadCompetenciaCommand();
        command.setCompetenciaId(1L);
        command.setEspecialidadId(2L);

        CompetenciaEspecialidad existente = new CompetenciaEspecialidad(1L, 2L);

        given(competenciaEspecialidadRepository.findByCompetenciaId(command.getCompetenciaId())).willReturn(Optional.of(existente));

        assertThrows(CompetenciaEspecialidadDuplicadaException.class, () ->
                asignarEspecialidadCompetenciaUseCase.asignarEspecialidadCompetencia(command));

        then(competenciaEspecialidadRepository).should(never()).saveCompetenciaEspecialidad(any());
        System.out.println("Se lanza CompetenciaEspecialidadDuplicadaException correctamente al intentar asignar una especialidad a una competencia que ya tiene asignacion");
    }

    @Test
    @DisplayName("Asignar especialidad a competencia")
    void asignar_especialidad_competencia() {
        AsignarEspecialidadCompetenciaCommand command = new AsignarEspecialidadCompetenciaCommand();
        command.setCompetenciaId(1L);
        command.setEspecialidadId(2L);

        CompetenciaEspecialidad asignacion = new CompetenciaEspecialidad(command.getCompetenciaId(), command.getEspecialidadId());

        given(competenciaEspecialidadRepository.findByCompetenciaId(command.getCompetenciaId())).willReturn(Optional.empty());
        given(competenciaEspecialidadRepository.saveCompetenciaEspecialidad(any())).willReturn(asignacion);

        CompetenciaEspecialidad resultado = asignarEspecialidadCompetenciaUseCase.asignarEspecialidadCompetencia(command);

        assertNotNull(resultado);
        assertEquals(command.getCompetenciaId(), resultado.getCompetenciaId());
        assertEquals(command.getEspecialidadId(), resultado.getEspecialidadId());

        then(competenciaEspecialidadRepository).should().saveCompetenciaEspecialidad(any());
        System.out.println("Se ha asignado la especialidad a la competencia correctamente competenciaId " + resultado.getCompetenciaId() + " especialidadId " + resultado.getEspecialidadId());
    }

    @Test
    @DisplayName("Intenta desasignar especialidad competencia no encontrada")
    void intento_desasignar_especialidad_competencia_noEncontrada() {
        given(competenciaEspecialidadRepository.findByCompetenciaId(1L)).willReturn(Optional.empty());

        assertThrows(CompetenciaEspecialidadNoEncontradaException.class, () ->
                desasignarEspecialidadCompetenciaUseCase.desasignarEspecialidadCompetencia(1L));

        then(competenciaEspecialidadRepository).should(never()).eliminarCompetenciaEspecialidad(any());
        System.out.println("Se lanza CompetenciaEspecialidadNoEncontradaException correctamente al intentar desasignar una especialidad no encontrada");
    }

    @Test
    @DisplayName("Desasignar especialidad a competencia")
    void desasignar_especialidad_competencia() {
        CompetenciaEspecialidad existente = new CompetenciaEspecialidad(1L, 2L);

        given(competenciaEspecialidadRepository.findByCompetenciaId(1L)).willReturn(Optional.of(existente));

        desasignarEspecialidadCompetenciaUseCase.desasignarEspecialidadCompetencia(1L);

        System.out.println("Especialidad desasignada de la competencia correctamente competenciaId 1");
        then(competenciaEspecialidadRepository).should().eliminarCompetenciaEspecialidad(1L);
    }

    @Test
    @DisplayName("Intenta editar especialidad competencia no encontrada")
    void intento_editar_especialidad_competencia_noEncontrada() {
        EditarEspecialidadCompetenciaCommand command = new EditarEspecialidadCompetenciaCommand();
        command.setEspecialidadId(3L);

        given(competenciaEspecialidadRepository.findByCompetenciaId(1L)).willReturn(Optional.empty());

        assertThrows(CompetenciaEspecialidadNoEncontradaException.class, () ->
                editarEspecialidadCompetenciaUseCase.editarEspecialidadCompetencia(command, 1L));

        then(competenciaEspecialidadRepository).should(never()).saveCompetenciaEspecialidad(any());
        System.out.println("Se lanza CompetenciaEspecialidadNoEncontradaException correctamente al intentar editar una asignacion inexistente");
    }

    @Test
    @DisplayName("Editar especialidad competencia")
    void editar_especialidad_competencia() {
        EditarEspecialidadCompetenciaCommand command = new EditarEspecialidadCompetenciaCommand();
        command.setEspecialidadId(3L);

        CompetenciaEspecialidad existente = new CompetenciaEspecialidad(1L, 2L);
        CompetenciaEspecialidad actualizada = new CompetenciaEspecialidad(1L, 3L);

        given(competenciaEspecialidadRepository.findByCompetenciaId(1L)).willReturn(Optional.of(existente));
        given(competenciaEspecialidadRepository.saveCompetenciaEspecialidad(any())).willReturn(actualizada);

        CompetenciaEspecialidad resultado = editarEspecialidadCompetenciaUseCase.editarEspecialidadCompetencia(command, 1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getCompetenciaId());
        assertEquals(3L, resultado.getEspecialidadId());

        then(competenciaEspecialidadRepository).should().saveCompetenciaEspecialidad(any());
        System.out.println("Se ha editado la especialidad de la competencia correctamente competenciaId " + resultado.getCompetenciaId() + " nuevaEspecialidadId " + resultado.getEspecialidadId());
    }

    @Test
    @DisplayName("Listar especialidades competencias")
    void listar_especialidades_competencias() {
        List<CompetenciaEspecialidad> lista = new ArrayList<>();

        CompetenciaEspecialidad ce1 = new CompetenciaEspecialidad(1L, 10L);
        CompetenciaEspecialidad ce2 = new CompetenciaEspecialidad(2L, 20L);

        lista.add(ce1);
        lista.add(ce2);

        given(competenciaEspecialidadRepository.findAll()).willReturn(lista);

        List<CompetenciaEspecialidad> resultado = listarEspecialidadesCompetenciaUseCase.listarEspecialidadesCompetencias();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(ce -> System.out.println("Asignacion listada correctamente: competenciaId " + ce.getCompetenciaId() + " especialidadId " + ce.getEspecialidadId()));
        then(competenciaEspecialidadRepository).should().findAll();
    }
}
