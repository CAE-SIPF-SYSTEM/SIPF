package com.caeproject.cae.application.usecases.programacionacademica.programaconmanual;

import com.caeproject.cae.application.usecases.programacionacademica.*;
import com.caeproject.cae.application.usecases.programacionacademica.commands.CrearProgramacionCommand;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.rapexception.RapNoEncontradoException;
import com.caeproject.cae.domain.ports.in.asignarinstructor.AsignarInstructorInputPort;

import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.out.*;

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
public class ProgramacionAcademicaUseCaseTest {

    @Mock
    private ProgramacionAcademicaRepository programacionAcademicaRepository;

    @Mock
    private TrimestreRepository trimestreRepository;

    @Mock
    private RapRepository rapRepository;

    @Mock
    private AsignarInstructorInputPort asignarInstructorInputPort;

    @Mock
    private FichaRepository fichaRepository;

    @Mock
    private ProgramaRepository programaRepository;

    @InjectMocks
    private CrearProgramacionAcademicaUseCase crearProgramacionAcademicaUseCase;

    @InjectMocks
    private EliminarProgramacionAcademicaUseCase eliminarProgramacionAcademicaUseCase;

    @InjectMocks
    private ListarProgramacionAcademicaUseCase listarProgramacionAcademicaUseCase;

    @InjectMocks
    private ObtenerProgramacionAcademicaUseCase obtenerProgramacionAcademicaUseCase;

    @InjectMocks
    private ObtenerResumenFichaUseCase obtenerResumenFichaUseCase;

    @InjectMocks
    private ObtenerResumenProgramaUseCase obtenerResumenProgramaUseCase;

    @Test
    @DisplayName("Intentar crear programacion academica rap no encontrado")
    void crear_programacionacademica_rap_noEncontrado() {
        CrearProgramacionCommand command = new CrearProgramacionCommand();
        command.setRapId(1L);
        command.setTrimestreId(10L);
        command.setUsuarioId(100L);
        command.setProgramaId(5L);
        command.setFichaId(3L);

        given(rapRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(RapNoEncontradoException.class, () -> crearProgramacionAcademicaUseCase.programacionAcademica(command));

        then(programacionAcademicaRepository).should(never()).saveProgramacion(any());
        System.out.println("Se lanza RuntimeException correctamente al intentar crear programacion con RAP no encontrado");
        System.out.println(RapNoEncontradoException.class);
        System.out.println("Datos intentados:  " + "RAP" + " " + command.getRapId() + " " + "ṔROGRAMA " + command.getProgramaId() + " " + "TRIMESTRE " + command.getTrimestreId() + " " + "USUARIO " + command.getUsuarioId() + " " + "FICHA " + command.getFichaId());
    }

    @Test
    @DisplayName("Intentar crear programacion academica trimestre no encontrado")
    void crear_programacionacademica_trimestre_noEncontrado() {
        CrearProgramacionCommand command = new CrearProgramacionCommand();
        command.setRapId(1L);
        command.setTrimestreId(10L);
        command.setUsuarioId(100L);
        command.setProgramaId(5L);
        command.setFichaId(3L);

        Rap rap = new Rap();
        rap.setId(1L);
        rap.setCompetenciaId(20L);

        given(rapRepository.findById(1L)).willReturn(Optional.of(rap));
        given(trimestreRepository.findById(10L)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                crearProgramacionAcademicaUseCase.programacionAcademica(command));

        then(programacionAcademicaRepository).should(never()).saveProgramacion(any());
        System.out.println("Se lanza RuntimeException correctamente al intentar crear programacion con Trimestre no encontrado");
        System.out.println("Datos intentados:  " + "RAP" + " " + command.getRapId() + " " + "ṔROGRAMA " + command.getProgramaId() + " " + "TRIMESTRE " + command.getTrimestreId() + " " + "USUARIO " + command.getUsuarioId() + " " + "FICHA " + command.getFichaId());    }

    @Test
    @DisplayName("Crear programacion academica correctamente")
    void crear_programacionacademica_correctamente() {
        CrearProgramacionCommand command = new CrearProgramacionCommand();
        command.setRapId(1L);
        command.setTrimestreId(10L);
        command.setUsuarioId(100L);
        command.setProgramaId(5L);
        command.setFichaId(2L);

        Rap rap = new Rap();
        rap.setId(1L);
        rap.setCompetenciaId(20L);

        Trimestre trimestre = new Trimestre();
        trimestre.setId(10L);

        ProgramacionAcademica guardada = new ProgramacionAcademica();
        guardada.setId(1L);
        guardada.setRapId(command.getRapId());
        guardada.setTrimestreId(command.getTrimestreId());
        guardada.setUsuarioId(command.getUsuarioId());
        guardada.setProgramaId(command.getProgramaId());

        given(rapRepository.findById(1L)).willReturn(Optional.of(rap));
        given(trimestreRepository.findById(10L)).willReturn(Optional.of(trimestre));
        given(programacionAcademicaRepository.saveProgramacion(any())).willReturn(guardada);

        ProgramacionAcademica resultado = crearProgramacionAcademicaUseCase.programacionAcademica(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getRapId());
        assertEquals(10L, resultado.getTrimestreId());
        assertEquals(100L, resultado.getUsuarioId());
        assertEquals(2L, command.getFichaId());

        then(asignarInstructorInputPort).should().asignarInstructor(any());
        then(programacionAcademicaRepository).should().saveProgramacion(any());
        System.out.println("Programacion academica creada correctamente ID:  " + resultado.getId() + " RAP " + resultado.getRapId() + " TRIMESTRE " + resultado.getTrimestreId() + " " + "USUARIO "+ command.getUsuarioId());
    }

    @Test
    @DisplayName("Eliminar programacion academica")
    void eliminar_programacionacademica() {
        eliminarProgramacionAcademicaUseCase.eliminarProgramacion(1L);

        then(programacionAcademicaRepository).should().eliminarProgramacionAcademica(1L);
        System.out.println("Programacion academica eliminada correctamente id 1");
    }

    @Test
    @DisplayName("Listar programaciones academicas")
    void listar_programacionesacademicas() {
        List<ProgramacionAcademica> lista = new ArrayList<>();

        ProgramacionAcademica p1 = new ProgramacionAcademica();
        p1.setId(1L);
        p1.setRapId(1L);
        p1.setTrimestreId(10L);

        lista.add(p1);

        given(programacionAcademicaRepository.findAll()).willReturn(lista);

        List<ProgramacionAcademica> resultado = listarProgramacionAcademicaUseCase.listarProgramaciones();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        then(programacionAcademicaRepository).should().findAll();
        System.out.println("Programaciones academicas listadas correctamente total " + resultado.size());
    }

    @Test
    @DisplayName("Obtener programacion academica por id no encontrado")
    void obtener_programacionacademica_id_noEncontrado() {
        given(programacionAcademicaRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(ProgramaNoEncontradoException.class, () ->
                obtenerProgramacionAcademicaUseCase.obtenerProgramacion(1L));

        System.out.println("Se lanza ProgramaNoEncontradoException correctamente al consultar ID inexistente");
    }

    @Test
    @DisplayName("Obtener resumen ficha no encontrada")
    void obtener_resumen_ficha_noEncontrada() {
        given(fichaRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(FichaNoEncontradaException.class, () ->
                obtenerResumenFichaUseCase.obtenerResumenFicha(1L));

        System.out.println("Se lanza FichaNoEncontradaException correctamente al obtener resumen de ficha inexistente");
    }

    @Test
    @DisplayName("Obtener resumen programa no encontrado")
    void obtener_resumen_programa_noEncontrado() {
        given(programaRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                obtenerResumenProgramaUseCase.obtenerResumenPrograma(1L));

        System.out.println("Se lanza RuntimeException correctamente al obtener resumen de programa inexistente");
    }
}