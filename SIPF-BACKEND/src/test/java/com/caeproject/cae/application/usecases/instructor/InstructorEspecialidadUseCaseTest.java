package com.caeproject.cae.application.usecases.instructor;

import com.caeproject.cae.application.usecases.especialidadinstructor.AsignarEspecialidadInstructorUseCase;
import com.caeproject.cae.application.usecases.especialidadinstructor.DesasignarEspecialidadInstructorUseCase;
import com.caeproject.cae.application.usecases.especialidadinstructor.EditarEspecialidadInstructorUseCase;
import com.caeproject.cae.application.usecases.especialidadinstructor.ListarInstructoresEspecialidadesUseCase;
import com.caeproject.cae.application.usecases.especialidadinstructor.commands.AsignarEspecialidadInstructorCommand;
import com.caeproject.cae.application.usecases.especialidadinstructor.commands.EditarEspecialidadInstructorCommand;
import com.caeproject.cae.domain.ports.exceptions.instructorespecialidadexception.InstructorEspecialidadDuplicadaException;
import com.caeproject.cae.domain.ports.exceptions.instructorespecialidadexception.InstructorEspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;
import com.caeproject.cae.domain.ports.out.InstructorEspecialidadRepository;
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
public class InstructorEspecialidadUseCaseTest {

    @Mock
    private InstructorEspecialidadRepository instructorEspecialidadRepository;

    @InjectMocks
    private AsignarEspecialidadInstructorUseCase asignarEspecialidadInstructorUseCase;

    @InjectMocks
    private DesasignarEspecialidadInstructorUseCase desasignarEspecialidadInstructorUseCase;

    @InjectMocks
    private EditarEspecialidadInstructorUseCase editarEspecialidadInstructorUseCase;

    @InjectMocks
    private ListarInstructoresEspecialidadesUseCase listarInstructoresEspecialidadesUseCase;

    @Test
    @DisplayName("Asignar especialidad a instructor duplicada")
    void asignar_especialidad_instructor_duplicada() {
        AsignarEspecialidadInstructorCommand command = new AsignarEspecialidadInstructorCommand();
        command.setUsuarioId(1L);
        command.setEspecialidadId(10L);

        InstructorEspecialidad existente = new InstructorEspecialidad();
        existente.setUsuarioId(1L);
        existente.setEspecialidadId(10L);

        given(instructorEspecialidadRepository.findByInstructorId(command.getUsuarioId())).willReturn(Optional.of(existente));

        assertThrows(InstructorEspecialidadDuplicadaException.class, () ->
                asignarEspecialidadInstructorUseCase.asignarEspecialidadInstructor(command));

        then(instructorEspecialidadRepository).should(never()).saveInstructorEspecialidad(any());
        System.out.println("Se lanza InstructorEspecialidadDuplicadaException correctamente al intentar asignar una especialidad a un instructor que ya la tiene asignada");
    }

    @Test
    @DisplayName("Asignar especialidad a instructor")
    void asignar_especialidad_instructor() {
        AsignarEspecialidadInstructorCommand command = new AsignarEspecialidadInstructorCommand();
        command.setUsuarioId(1L);
        command.setEspecialidadId(10L);

        InstructorEspecialidad asignacion = new InstructorEspecialidad();
        asignacion.setUsuarioId(command.getUsuarioId());
        asignacion.setEspecialidadId(command.getEspecialidadId());

        given(instructorEspecialidadRepository.findByInstructorId(command.getUsuarioId())).willReturn(Optional.empty());
        given(instructorEspecialidadRepository.saveInstructorEspecialidad(any())).willReturn(asignacion);

        InstructorEspecialidad resultado = asignarEspecialidadInstructorUseCase.asignarEspecialidadInstructor(command);

        assertNotNull(resultado);
        assertEquals(command.getUsuarioId(), resultado.getUsuarioId());
        assertEquals(command.getEspecialidadId(), resultado.getEspecialidadId());

        then(instructorEspecialidadRepository).should().saveInstructorEspecialidad(any());
        System.out.println("Se ha asignado la especialidad al instructor correctamente usuarioId " + resultado.getUsuarioId() + " especialidadId " + resultado.getEspecialidadId());
    }

    @Test
    @DisplayName("Intenta desasignar especialidad a instructor no encontrada")
    void intento_desasignar_especialidad_instructor_noEncontrada() {
        given(instructorEspecialidadRepository.findByInstructorId(1L)).willReturn(Optional.empty());

        assertThrows(InstructorEspecialidadNoEncontradaException.class, () ->
                desasignarEspecialidadInstructorUseCase.desasignarInstructorEspecialidad(1L));

        then(instructorEspecialidadRepository).should(never()).eliminarInstructorEspecialidad(any());
        System.out.println("Se lanza InstructorEspecialidadNoEncontradaException correctamente al intentar desasignar una especialidad no encontrada");
    }

    @Test
    @DisplayName("Desasignar especialidad a instructor")
    void desasignar_especialidad_instructor() {
        InstructorEspecialidad existente = new InstructorEspecialidad();
        existente.setUsuarioId(1L);
        existente.setEspecialidadId(10L);

        given(instructorEspecialidadRepository.findByInstructorId(1L)).willReturn(Optional.of(existente));

        desasignarEspecialidadInstructorUseCase.desasignarInstructorEspecialidad(1L);

        System.out.println("Especialidad desasignada del instructor correctamente usuarioId 1");
        then(instructorEspecialidadRepository).should().eliminarInstructorEspecialidad(1L);
    }

    @Test
    @DisplayName("Intenta editar especialidad de instructor no encontrada")
    void intento_editar_especialidad_instructor_noEncontrada() {
        EditarEspecialidadInstructorCommand command = new EditarEspecialidadInstructorCommand();
        command.setUsuarioId(1L);
        command.setEspecialidadId(20L);

        given(instructorEspecialidadRepository.findByInstructorId(1L)).willReturn(Optional.empty());

        assertThrows(InstructorEspecialidadNoEncontradaException.class, () ->
                editarEspecialidadInstructorUseCase.editarInstructorEspecialidad(command, 1L));

        then(instructorEspecialidadRepository).should(never()).saveInstructorEspecialidad(any());
        System.out.println("Se lanza InstructorEspecialidadNoEncontradaException correctamente al intentar editar la especialidad de un instructor no encontrado");
    }

    @Test
    @DisplayName("Editar especialidad de instructor")
    void editar_especialidad_instructor() {
        EditarEspecialidadInstructorCommand command = new EditarEspecialidadInstructorCommand();
        command.setUsuarioId(1L);
        command.setEspecialidadId(20L);

        InstructorEspecialidad existente = new InstructorEspecialidad();
        existente.setUsuarioId(1L);
        existente.setEspecialidadId(10L);

        InstructorEspecialidad actualizada = new InstructorEspecialidad();
        actualizada.setUsuarioId(1L);
        actualizada.setEspecialidadId(20L);

        given(instructorEspecialidadRepository.findByInstructorId(1L)).willReturn(Optional.of(existente));
        given(instructorEspecialidadRepository.saveInstructorEspecialidad(any())).willReturn(actualizada);

        InstructorEspecialidad resultado = editarEspecialidadInstructorUseCase.editarInstructorEspecialidad(command, 1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getUsuarioId());
        assertEquals(20L, resultado.getEspecialidadId());

        then(instructorEspecialidadRepository).should().saveInstructorEspecialidad(any());
        System.out.println("Se ha editado la especialidad del instructor correctamente usuarioId " + resultado.getUsuarioId() + " nuevaEspecialidadId " + resultado.getEspecialidadId());
    }

    @Test
    @DisplayName("Listar especialidades de instructores")
    void s() {
        List<InstructorEspecialidad> lista = new ArrayList<>();

        InstructorEspecialidad ie1 = new InstructorEspecialidad();
        ie1.setUsuarioId(1L);
        ie1.setEspecialidadId(10L);

        InstructorEspecialidad ie2 = new InstructorEspecialidad();
        ie2.setUsuarioId(2L);
        ie2.setEspecialidadId(20L);

        lista.add(ie1);
        lista.add(ie2);

        given(instructorEspecialidadRepository.findAll()).willReturn(lista);

        List<InstructorEspecialidad> resultado = listarInstructoresEspecialidadesUseCase.listarInstructorEspecialidad();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(ie -> System.out.println("Asignacion listada correctamente: usuarioId " + ie.getUsuarioId() + " especialidadId " + ie.getEspecialidadId()));
        then(instructorEspecialidadRepository).should().findAll();
    }
}
