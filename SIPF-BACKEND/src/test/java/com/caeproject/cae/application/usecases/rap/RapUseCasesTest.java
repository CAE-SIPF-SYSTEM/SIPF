package com.caeproject.cae.application.usecases.rap;

import com.caeproject.cae.application.usecases.rap.commands.CrearRapCommand;
import com.caeproject.cae.domain.ports.exceptions.rapexception.RapDuplicadoException;
import com.caeproject.cae.domain.ports.exceptions.rapexception.RapNoEncontradoException;
import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.domain.ports.out.RapRepository;
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
public class RapUseCasesTest {

    @Mock
    private RapRepository rapRepository;

    @InjectMocks
    private CrearRapUseCase crearRapUseCase;

    @InjectMocks
    private ListarRapsUseCase listarRapsUseCase;

    @InjectMocks
    private EliminarRapUseCase eliminarRapUseCase;

    @InjectMocks
    private ObtenerRapUseCase obtenerRapUseCase;

    @Test
    @DisplayName("Crear rap duplicado")
    void crear_rap_duplicado() {
        CrearRapCommand command = new CrearRapCommand();
        command.setCompetenciaId(1L);
        command.setDescripcion("Especificar requisitos del software");

        given(rapRepository.existByCompetenciaId(command.getCompetenciaId())).willReturn(true);

        assertThrows(RapDuplicadoException.class, () -> crearRapUseCase.createRap(command));

        then(rapRepository).should(never()).saveRap(any());
        System.out.println("Se lanza RapDuplicadoException correctamente al intentar crear el RAP con una competencia ya existente");
    }

    @Test
    @DisplayName("Crear rap")
    void crear_rap() {
        CrearRapCommand command = new CrearRapCommand();
        command.setCompetenciaId(1L);
        command.setDescripcion("Especificar requisitos del software");

        Rap rap = new Rap();
        rap.setId(1L);
        rap.setCompetenciaId(command.getCompetenciaId());
        rap.setDescripcion(command.getDescripcion());


        given(rapRepository.existByCompetenciaId(command.getCompetenciaId())).willReturn(false);
        given(rapRepository.saveRap(any())).willReturn(rap);

        Rap resultado = crearRapUseCase.createRap(command);

        assertNotNull(resultado);
        assertEquals(command.getDescripcion(), resultado.getDescripcion());
        assertEquals(command.getCompetenciaId(), resultado.getCompetenciaId());

        then(rapRepository).should().saveRap(any());
        System.out.println("Se ha creado el RAP correctamente id " + resultado.getId() + " descripcion " + resultado.getDescripcion() + " competenciaId " + resultado.getCompetenciaId());
    }

    @Test
    @DisplayName("Intenta eliminar rap no encontrado")
    void intento_eliminar_rapNoEncontrado() {
        given(rapRepository.findById(1L)).willReturn(Optional.empty());

        assertThrows(RapNoEncontradoException.class, () -> eliminarRapUseCase.eliminarRap(1L));

        then(rapRepository).should(never()).eliminarRap(any());
        System.out.println("Se lanza RapNoEncontradoException correctamente al intentar eliminar el RAP inexistente");
    }

    @Test
    @DisplayName("Eliminar rap")
    void eliminar_rap() {
        Rap rap = new Rap();
        rap.setId(1L);
        rap.setCompetenciaId(1L);
        rap.setDescripcion("Disenar la solucion de software");


        given(rapRepository.findById(1L)).willReturn(Optional.of(rap));

        eliminarRapUseCase.eliminarRap(1L);

        System.out.println("RAP eliminado correctamente datos id " + rap.getId() + " descripcion " + rap.getDescripcion());
        then(rapRepository).should().eliminarRap(rap.getId());
    }

    @Test
    @DisplayName("Listar raps")
    void listar_raps() {
        List<Rap> raps = new ArrayList<>();

        Rap r1 = new Rap();
        r1.setId(1L);
        r1.setCompetenciaId(1L);
        r1.setDescripcion("RAP 1");

        Rap r2 = new Rap();
        r2.setId(2L);
        r2.setCompetenciaId(1L);
        r2.setDescripcion("RAP 2");

        raps.add(r1);
        raps.add(r2);

        given(rapRepository.findAll()).willReturn(raps);

        List<Rap> resultado = listarRapsUseCase.listarRaps();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(r -> System.out.println("RAP listado correctamente id " + r.getId() + " descripcion " + r.getDescripcion()));
        then(rapRepository).should().findAll();
    }

    @Test
    @DisplayName("Obtener rap por competencia")
    void obtener_rap_por_competencia() {
        Rap r1 = new Rap();
        r1.setId(1L);
        r1.setCompetenciaId(10L);
        r1.setDescripcion("RAP Competencia 10 - A");

        Rap r2 = new Rap();
        r2.setId(2L);
        r2.setCompetenciaId(10L);
        r2.setDescripcion("RAP Competencia 10 - B");

        List<Rap> rapsCompetencia = List.of(r1, r2);

        given(rapRepository.findByCompetencia(10L)).willReturn(rapsCompetencia);

        List<Rap> resultado = obtenerRapUseCase.obtenerRapCompetencia(10L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(r -> System.out.println("RAP por competencia: id " + r.getId() + " competenciaId " + r.getCompetenciaId() + " descripcion " + r.getDescripcion()));
        then(rapRepository).should().findByCompetencia(10L);
    }
}
