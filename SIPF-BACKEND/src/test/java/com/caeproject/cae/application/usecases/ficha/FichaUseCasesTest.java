package com.caeproject.cae.application.usecases.ficha;


import com.caeproject.cae.application.usecases.ficha.commands.RegistrarFichaCommand;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaDuplicadaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaInvalidaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.model.Ficha;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.any;
import static org.mockito.Mockito.never;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class FichaUseCasesTest {

    @Mock
    private ProgramaRepository programaRepository;
    @Mock
    private FichaRepository fichaRepository;
    @InjectMocks
    private CrearFichaUseCase crearFichaUseCase;
    @InjectMocks
    private EliminarFichaUseCase eliminarFichaUseCase;
    @InjectMocks
    private ListarFichasUseCase listarFichasUseCase;

    @InjectMocks
    private ObtenerFichaUseCase obtenerFichaUseCase;


    @Test
    @DisplayName("Crear ficha con numero de ficha repetido")
    void crear_ficha_numerorepetido_lanzaFichaDuplicadaException() throws ParseException {
        //objeto date
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        Date fechaInicio = formato.parse("2024-06-23");
        Date fechaFin = formato.parse("2026-10-23");

        RegistrarFichaCommand commad = new RegistrarFichaCommand();
        commad.setProgramaId(1L);
        commad.setCodigoFicha("2996315");
        commad.setFechaInicio(fechaInicio);
        commad.setFechaFin(fechaFin);

        given(fichaRepository.existByCodigoFicha("2996315")).willReturn(true);

        assertThrows(FichaDuplicadaException.class, () ->
                crearFichaUseCase.registrarFicha(commad));

        then(fichaRepository).should(never()).saveFicha(any());
        System.out.println("Se lanza FichaDuplicadaException correctamente al intentar crear una ficha con numero de ficha repetido - Ficha:  " + commad.getCodigoFicha());
        System.out.println(FichaDuplicadaException.class);

    }

    @Test
    @DisplayName("Crear ficha con fecha inicio superior a fecha de finalizacion")
    void crear_ficha_fechainicioMayor_fechaFin() throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        Date fechaInicio = formato.parse("2026-10-24");
        Date fechaFin = formato.parse("2026-10-23");

        RegistrarFichaCommand command = new RegistrarFichaCommand();
        command.setProgramaId(1L);
        command.setCodigoFicha("2996315");
        command.setFechaInicio(fechaInicio);
        command.setFechaFin(fechaFin);


        assertThrows(FichaInvalidaException.class, () ->
                crearFichaUseCase.registrarFicha(command));

        then(fichaRepository).should(never()).saveFicha(any());
        System.out.println("Se lanza FichaInvalidaException correctamente al intentar crear una ficha con fecha inicio superior a fecha de finalizacion - Fecha Inicio:  " + command.getFechaInicio() + " Fecha Fin: " + command.getFechaFin());
    }


    @Test
    @DisplayName("Crear ficha con programa no encontrado")
    void crear_ficha_programaNoEncontrado() throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        Date fechaInicio = formato.parse("2024-10-24");
        Date fechaFin = formato.parse("2026-10-23");

        RegistrarFichaCommand command = new RegistrarFichaCommand();
        command.setProgramaId(1L);
        command.setCodigoFicha("2996315");
        command.setFechaInicio(fechaInicio);
        command.setFechaFin(fechaFin);

        given(programaRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(ProgramaNoEncontradoException.class, () ->
                crearFichaUseCase.registrarFicha(command));
        System.out.println("Se lanza ProgramaNoEncontradoException correctamente al intentar crear una ficha con un programa no encontrado - Programa ID: " + command.getProgramaId());
    }

    @Test
    @DisplayName("Crear ficha fechas nulas")
    void crear_ficha_fechasNulas(){


        RegistrarFichaCommand command = new RegistrarFichaCommand();
        command.setProgramaId(1L);
        command.setCodigoFicha("2996315");
        command.setFechaInicio(null);
        command.setFechaFin(null);

        assertThrows(FichaInvalidaException.class, () ->
                crearFichaUseCase.registrarFicha(command));
        System.out.println("Se lanza FichaInvalidaException correctamente al intentar crear una ficha con fechas nulas" + command.getCodigoFicha() + "fechas digitadas " + command.getFechaFin() + command.getFechaFin());

    }

    @Test
    @DisplayName("Crear ficha correctamente")
    void crear_ficha_correctamente() throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        Date fechaInicio = formato.parse("2024-06-24");
        Date fechaFin = formato.parse("2026-10-23");

        RegistrarFichaCommand command = new RegistrarFichaCommand();
        command.setProgramaId(1L);
        command.setCodigoFicha("2996315");
        command.setFechaInicio(fechaInicio);
        command.setFechaFin(fechaFin);

        Ficha nuevaFicha = new Ficha();
        nuevaFicha.setId(1L);
        nuevaFicha.setCodigoFicha(command.getCodigoFicha());
        nuevaFicha.setProgramaId(command.getProgramaId());

        given(fichaRepository.existByCodigoFicha("2996315")).willReturn(false);
        given(programaRepository.findById(1L)).willReturn(Optional.of(new Programa()));
        given((fichaRepository.saveFicha(any(Ficha.class)))).willReturn(nuevaFicha);

        Ficha resultado = crearFichaUseCase.registrarFicha(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("2996315", resultado.getCodigoFicha());
        then(fichaRepository).should().saveFicha(any(Ficha.class));

        System.out.println("Se ha creado la ficha correctamente Ficha :  " + resultado.getCodigoFicha() + (" ") + resultado.getProgramaId());
    }

    @Test
    @DisplayName("Intento eliminar ficha no encontrada")
    void eliminar_ficha_no_encontrada(){

        Ficha nuevaficha = new Ficha();
        nuevaficha.setId(1L);

        given(fichaRepository.findById(99L)).willReturn(Optional.empty());
        assertThrows(FichaNoEncontradaException.class, () ->
                eliminarFichaUseCase.eliminarFicha(99L));

        then(fichaRepository).should(never()).deleteFicha(any());
        System.out.println("Se lanza FichaNoEncontradaException correctamente al intentar eliminar una ficha no encontrada - Ficha ID: " + nuevaficha.getId());
    }

    @Test
    @DisplayName("ELiminar ficha correctamente")
    void eliminar_ficha(){
        Ficha f = new Ficha();
        f.setId(1L);
        f.setProgramaId(1L);
        f.setCodigoFicha("2996315");
        f.setFechaInicio(new Date());
        f.setFechaFin(new Date());

        given(fichaRepository.findById(1L)).willReturn(Optional.of(f));
        System.out.println("Ficha eliinada correctamente datos de ficha:  " + f.getId() + (" ") + f.getCodigoFicha() + (" ") + f.getProgramaId() + (" ") + f.getFechaInicio() + (" ") + f.getFechaFin());

        eliminarFichaUseCase.eliminarFicha(f.getId());
        then(fichaRepository).should().deleteFicha(f.getId());

    }

    @Test
    @DisplayName("Listar fichas")
    void listar_fichas(){
        Ficha ficha1 = new Ficha();
        ficha1.setId(1L);
        ficha1.setCodigoFicha("296352");
        ficha1.setFechaInicio(new Date());
        ficha1.setFechaFin(new Date());
        ficha1.setProgramaId(1L);

        Ficha ficha2 = new Ficha();
        ficha2.setId(1L);
        ficha2.setCodigoFicha("2963252");
        ficha2.setFechaInicio(new Date());
        ficha2.setFechaFin(new Date());
        ficha2.setProgramaId(1L);

        given(fichaRepository.findAll()).willReturn(List.of(ficha1, ficha2));

        List<Ficha> resulado = listarFichasUseCase.listarFichas();

        assertNotNull(resulado);
        assertEquals(2, resulado.size());
        assertEquals("296352", resulado.get(0).getCodigoFicha());
        assertEquals("2963252", resulado.get(1).getCodigoFicha());
        then(fichaRepository).should().findAll();
        System.out.println("Se listan las fichas correctamente, cantidad de fichas: "  + resulado.size() + " Ficha 1: " + resulado.get(0).getCodigoFicha() + " Ficha 2: " + resulado.get(1).getCodigoFicha());
    }

    @Test
    @DisplayName("Listar lista vacia")
    void listarFichas_vacias(){

        given(fichaRepository.findAll()).willReturn(new ArrayList<>());

        List<Ficha> resultado = listarFichasUseCase.listarFichas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        then(fichaRepository).should().findAll();
        System.out.println("Se listan las fichas correctamente, cantidad de fichas: "  + resultado.size() + " Lista vacia");
    }

    @Test
    @DisplayName("Obtener ficha por ID")
    void obtener_ficha_por_id(){
        Ficha ficha = new Ficha();
        ficha.setId(1L);
        ficha.setCodigoFicha("296352");
        ficha.setFechaInicio(new Date());
        ficha.setFechaFin(new Date());
        ficha.setProgramaId(1L);

        given(fichaRepository.findById(1L)).willReturn(Optional.of(ficha));

        Ficha resultado = obtenerFichaUseCase.obtenerFicha(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("296352", resultado.getCodigoFicha());
        then(fichaRepository).should().findById(1L);
        System.out.println("Se obtiene la ficha correctamente - Ficha ID: " + resultado.getId() + " Codigo: " + resultado.getCodigoFicha());
    }

    @Test
    @DisplayName("Obtener ficha por programaId")
    void obtener_ficha_por_programaId(){
        Ficha ficha = new Ficha();
        ficha.setId(1L);
        ficha.setCodigoFicha("2996315");
        ficha.setFechaInicio(new Date());
        ficha.setFechaFin(new Date());
        ficha.setProgramaId(1L);


        given(fichaRepository.findByProgramaId(1L)).willReturn(List.of(ficha));

        Ficha resultado = obtenerFichaUseCase.obtenerFichaPorProgramaId(1L).get(0);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("2996315", resultado.getCodigoFicha());
        then(fichaRepository).should().findByProgramaId(1L);
        System.out.println("Se obtuvo las ficha " + resultado.getProgramaId() + " Codigo " + resultado.getCodigoFicha() + " ProgramaId " + resultado.getProgramaId());
    }

}
