package com.caeproject.cae.application.usecases.trimestre;

import com.caeproject.cae.application.usecases.trimestre.commands.CrearTrimestreCommand;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaDuplicadaException;
import com.caeproject.cae.domain.ports.model.Trimestre;
import com.caeproject.cae.domain.ports.out.TrimestreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class TrimestreUseCasesTest {

    @Mock
    private TrimestreRepository trimestreRepository;

    @InjectMocks
    private CrearTrimestreUseCase crearTrimestreUseCase;

    @InjectMocks
    private ListarTrimestreUseCase listarTrimestreUseCase;

    @InjectMocks
    private EliminarTrimestreUseCase eliminarTrimestreUseCase;

    @InjectMocks
    private ObtenerTrimestreUseCase obtenerTrimestreUseCase;

    @Test
    @DisplayName("Crear trimestre")
    void crear_trimestre() {
        CrearTrimestreCommand command = new CrearTrimestreCommand();
        command.setFichaId(1L);
        command.setAnio(2024);
        command.setNumeroTrimestre(1);
        command.setFechaInicio(new Date(124, 0, 15));
        command.setFechaFin(new Date(124, 2, 30));

        Trimestre trimestre = new Trimestre();
        trimestre.setId(1L);
        trimestre.setFichaId(command.getFichaId());
        trimestre.setAnio(command.getAnio());
        trimestre.setNumeroTrimestre(command.getNumeroTrimestre());
        trimestre.setFechaInicio(command.getFechaInicio());
        trimestre.setFechaFin(command.getFechaFin());

        given(trimestreRepository.existsByFicha(command.getFichaId())).willReturn(false);
        given(trimestreRepository.savetrimestre(any())).willReturn(trimestre);

        Trimestre resultado = crearTrimestreUseCase.crearTrimestre(command);

        assertNotNull(resultado);
        assertEquals(command.getAnio(), resultado.getAnio());
        assertEquals(command.getNumeroTrimestre(), resultado.getNumeroTrimestre());
        assertEquals(command.getFichaId(), resultado.getFichaId());

        then(trimestreRepository).should().savetrimestre(any());
        System.out.println("Se ha creado el trimestre correctamente id " + resultado.getId() + " anio " + resultado.getAnio() + " trimestre " + resultado.getNumeroTrimestre() + " fichaId " + resultado.getFichaId());
    }

    @Test
    @DisplayName("Crear trimestre con anio nulo")
    void crear_trimestre_anioNulo() {
        CrearTrimestreCommand command = new CrearTrimestreCommand();
        command.setFichaId(1L);
        command.setAnio(null);
        command.setNumeroTrimestre(1);
        command.setFechaInicio(new Date(124, 0, 15));
        command.setFechaFin(new Date(124, 2, 30));

        assertThrows(IllegalArgumentException.class, () -> crearTrimestreUseCase.crearTrimestre(command));
        then(trimestreRepository).should(never()).savetrimestre(any());
        System.out.println("Se lanza IllegalArgumentException correctamente al intentar crear trimestre con anio nulo");
    }

    @Test
    @DisplayName("Crear trimestre con fechas nulas")
    void crear_trimestre_fechasNulas() {
        CrearTrimestreCommand command = new CrearTrimestreCommand();
        command.setFichaId(1L);
        command.setAnio(2024);
        command.setNumeroTrimestre(1);
        command.setFechaInicio(null);
        command.setFechaFin(null);

        assertThrows(IllegalArgumentException.class, () -> crearTrimestreUseCase.crearTrimestre(command));
        then(trimestreRepository).should(never()).savetrimestre(any());
        System.out.println("Se lanza IllegalArgumentException correctamente al intentar crear trimestre con fechas nulas");
    }

    @Test
    @DisplayName("Crear trimestre con fecha inicio mayor a fecha fin")
    void crear_trimestre_fechaInicioMayorAFechaFin() {
        CrearTrimestreCommand command = new CrearTrimestreCommand();
        command.setFichaId(1L);
        command.setAnio(2024);
        command.setNumeroTrimestre(1);
        command.setFechaInicio(new Date(124, 5, 15));
        command.setFechaFin(new Date(124, 2, 30));

        assertThrows(IllegalArgumentException.class, () -> crearTrimestreUseCase.crearTrimestre(command));
        then(trimestreRepository).should(never()).savetrimestre(any());
        System.out.println("Se lanza IllegalArgumentException correctamente al intentar crear trimestre con fecha inicio mayor a fecha fin");
    }

    @Test
    @DisplayName("Crear trimestre con ficha duplicada")
    void crear_trimestre_fichaDuplicada() {
        CrearTrimestreCommand command = new CrearTrimestreCommand();
        command.setFichaId(1L);
        command.setAnio(2024);
        command.setNumeroTrimestre(1);
        command.setFechaInicio(new Date(124, 0, 15));
        command.setFechaFin(new Date(124, 2, 30));

        given(trimestreRepository.existsByFicha(1L)).willReturn(true);

        assertThrows(FichaDuplicadaException.class, () -> crearTrimestreUseCase.crearTrimestre(command));
        then(trimestreRepository).should(never()).savetrimestre(any());
        System.out.println("Se lanza FichaDuplicadaException correctamente al intentar crear trimestre para una ficha existente");
    }

    @Test
    @DisplayName("Listar trimestres")
    void listar_trimestres() {
        List<Trimestre> trimestres = new ArrayList<>();

        Trimestre t1 = new Trimestre();
        t1.setId(1L);
        t1.setFichaId(1L);
        t1.setAnio(2024);
        t1.setNumeroTrimestre(1);

        Trimestre t2 = new Trimestre();
        t2.setId(2L);
        t2.setFichaId(1L);
        t2.setAnio(2024);
        t2.setNumeroTrimestre(2);

        trimestres.add(t1);
        trimestres.add(t2);

        given(trimestreRepository.findAll()).willReturn(trimestres);

        List<Trimestre> resultado = listarTrimestreUseCase.listarTrimestres();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(t -> System.out.println("Trimestre listado: Id " + t.getId() + " Anio " + t.getAnio() + " Numero " + t.getNumeroTrimestre() + " FichaId " + t.getFichaId()));
        then(trimestreRepository).should().findAll();
    }

    @Test
    @DisplayName("Obtener trimestre por id")
    void obtener_trimestre_por_id() {
        Trimestre t = new Trimestre();
        t.setId(1L);
        t.setFichaId(1L);
        t.setAnio(2024);
        t.setNumeroTrimestre(1);

        given(trimestreRepository.findById(1L)).willReturn(Optional.of(t));

        Trimestre resultado = obtenerTrimestreUseCase.obtenerTrimestre(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        System.out.println("Trimestre obtenido correctamente: Id " + resultado.getId() + " Anio " + resultado.getAnio() + " Numero " + resultado.getNumeroTrimestre());
        then(trimestreRepository).should().findById(1L);
    }

    @Test
    @DisplayName("Obtener trimestre por ficha existente")
    void obtener_trimestre_por_ficha_existente() {
        Trimestre t1 = new Trimestre();
        t1.setId(1L);
        t1.setFichaId(100L);
        t1.setAnio(2024);
        t1.setNumeroTrimestre(1);

        Trimestre t2 = new Trimestre();
        t2.setId(2L);
        t2.setFichaId(100L);
        t2.setAnio(2024);
        t2.setNumeroTrimestre(2);

        List<Trimestre> trimestresFicha = List.of(t1, t2);

        given(trimestreRepository.findByFicha(100L)).willReturn(trimestresFicha);

        List<Trimestre> resultado = obtenerTrimestreUseCase.obtenerTrimestreFicha(100L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(t -> System.out.println("Trimestre por ficha existente: Id " + t.getId() + " FichaId " + t.getFichaId() + " Numero " + t.getNumeroTrimestre()));
        then(trimestreRepository).should().findByFicha(100L);
    }

    @Test
    @DisplayName("Eliminar trimestre")
    void eliminar_trimestre() {
        Trimestre t = new Trimestre();
        t.setId(1L);
        t.setFichaId(1L);
        t.setAnio(2024);
        t.setNumeroTrimestre(1);

        given(trimestreRepository.findById(1L)).willReturn(Optional.of(t));

        eliminarTrimestreUseCase.eliminarTrimestre(1L);

        System.out.println("Trimestre eliminado correctamente Id " + t.getId());
        then(trimestreRepository).should().eliminarTrimestre(1L);
    }
}
