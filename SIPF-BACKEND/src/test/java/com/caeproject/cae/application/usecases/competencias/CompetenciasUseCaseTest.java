package com.caeproject.cae.application.usecases.competencias;
import com.caeproject.cae.application.usecases.competencia.CrearCompetenciaUseCase;
import com.caeproject.cae.application.usecases.competencia.EliminarCompetenciaUseCase;
import com.caeproject.cae.application.usecases.competencia.ListarCompetenciasUseCase;
import com.caeproject.cae.application.usecases.competencia.ObtenerCompetenciaUseCase;
import com.caeproject.cae.application.usecases.competencia.commands.CrearCompetenciaCommand;
import com.caeproject.cae.domain.ports.exceptions.competenciaexception.CompetenciaDuplicadaException;
import com.caeproject.cae.domain.ports.exceptions.competenciaexception.CompetenciaNoEncontradaException;
import com.caeproject.cae.domain.ports.model.Competencia;
import com.caeproject.cae.domain.ports.model.enums.TipoCompetencia;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
public class CompetenciasUseCaseTest {

    @Mock
    private  CompetenciaRepository competenciaRepository;

    @InjectMocks
    private  CrearCompetenciaCommand crearCompetenciaCommand;

    @InjectMocks
    private  CrearCompetenciaUseCase crearCompetenciaUseCase;

    @InjectMocks
    private  ListarCompetenciasUseCase listarCompetenciasUseCase;

    @InjectMocks
    private  EliminarCompetenciaUseCase eliminarCompetenciaUseCase;

    @InjectMocks
    private  ObtenerCompetenciaUseCase obtenerCompetenciaUseCase;

    @Test
    @DisplayName("Crear competencia duplicada")
    void crear_competencia_duplicada(){

        CrearCompetenciaCommand command = new CrearCompetenciaCommand();
        command.setNombre("Competencia 1");
        command.setTipoCompetencia(TipoCompetencia.TRANSVERSAL);

        given(competenciaRepository.existByName(command.getNombre())).willReturn(true);
        assertThrows(CompetenciaDuplicadaException.class, ()->
                crearCompetenciaUseCase.crearCompetencia(command));

        then(competenciaRepository).should(never()).saveCompetencia(any());
        System.out.println("Se lanza competenciaDuplicadaException correctamente al intentar crear la competencia con un nombre igual dentro de el sistema");
        System.out.println(CompetenciaDuplicadaException.class);

    }

    @Test
    @DisplayName("Crear competencia")
    void crear_competenia(){
        CrearCompetenciaCommand command = new CrearCompetenciaCommand();

        command.setNombre("Estructurar la solucion del software");
        command.setTipoCompetencia(TipoCompetencia.TECNICA);

        Competencia competencia = new Competencia();

        competencia.setId(1L);
        competencia.setCodigo("COMP-001");
        competencia.setNombre(command.getNombre());
        competencia.setTipoCompetencia(command.getTipoCompetencia());

        given(competenciaRepository.existByName(command.getNombre())).willReturn(false);

        given(competenciaRepository.saveCompetencia(any())).willReturn(competencia);

        Competencia resultado = crearCompetenciaUseCase.crearCompetencia(command);

        assertNotNull(resultado);
        assertEquals(command.getNombre(), resultado.getNombre());
        assertEquals(command.getTipoCompetencia(), resultado.getTipoCompetencia());

        then(competenciaRepository).should().saveCompetencia(any());
        System.out.println("Se ha creado la competencia correctamente  nombre " + resultado.getNombre() + (" ") + resultado.getTipoCompetencia() + (" ") + resultado.getCodigo() + (" ") + resultado.getId());
    }

    @Test
    @DisplayName("Intenta eliminar competencia No encontrada ")
    void intnto_eliminar_competenciaNoEncontrada(){

        given(competenciaRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(CompetenciaNoEncontradaException.class,()->
                eliminarCompetenciaUseCase.eliminarCompetencia(1L));

        then(competenciaRepository).should(never()).eliminarCompetencia(any());
        System.out.println("Se lanza competenciaNoEncontradaException correctamente al intentar eliminar la competencia");
    }

    @Test
    @DisplayName("Eliminar competencia")
    void eliminar_competencia(){
        Competencia c = new Competencia();
        c.setId(1L);
        c.setNombre("Desarrollar artefactos del software");
        c.setCodigo("COMP-002");
        c.setTipoCompetencia(TipoCompetencia.TECNICA);

        given(competenciaRepository.findById(1L)).willReturn(Optional.of(c));
        eliminarCompetenciaUseCase.eliminarCompetencia(1L);
        System.out.println("Competencia eliminada correcatamente datos  " + c.getId()  + (" ") + c.getCodigo() + (" ") + c.getNombre() + (" ") + c.getTipoCompetencia());
        then(competenciaRepository).should().eliminarCompetencia(c.getId());

    }

    @Test
    @DisplayName("Listar competencias ")
    void listar_competencias(){

        List<Competencia> competencias = new ArrayList<>();

        Competencia c1 = new Competencia();
        c1.setId(1L);
        c1.setCodigo("COMP-001");
        c1.setTipoCompetencia(TipoCompetencia.TRANSVERSAL);
        c1.setNombre("Competencia 1");

        Competencia c2 = new Competencia();
        c2.setId(2L);
        c2.setCodigo("COMP-002");
        c2.setTipoCompetencia(TipoCompetencia.TECNICA);
        c2.setNombre("Competencia 2");

        competencias.add(c1);
        competencias.add(c2);

        given(competenciaRepository.findAll()).willReturn(competencias);
        listarCompetenciasUseCase.listarCompetencia().forEach(competencia -> {
            System.out.println("Competencia listada correctamente datos  " + competencia.getId() + (" ") + competencia.getCodigo() + (" ") + competencia.getNombre() + (" ") + competencia.getTipoCompetencia());
        });
        then(competenciaRepository).should().findAll();
    }

    @Test
    @DisplayName("Listar competencias vacias")
    void listar_comeptencias_vacias(){
        List<Competencia> comepetencias = new ArrayList<>();

        given(competenciaRepository.findAll()).willReturn(comepetencias);
        List<Competencia> resultado = listarCompetenciasUseCase.listarCompetencia();
        assertTrue(resultado.isEmpty());
        System.out.println("Se listan competencias vacias correctamente");
        System.out.println("Lista " + resultado);
        then(competenciaRepository).should().findAll();
    }

    @Test
    @DisplayName("Obtener competencia")
    void obtener_competencia_portipo_competencia(){

        Competencia c1 = new Competencia();
        c1.setId(1L);
        c1.setCodigo("COMP-001");
        c1.setTipoCompetencia(TipoCompetencia.TRANSVERSAL);
        c1.setNombre("Competencia 1");
        Competencia c2 = new Competencia();
        c2.setId(2L);
        c2.setCodigo("COMP-002");
        c2.setTipoCompetencia(TipoCompetencia.TECNICA);
        c2.setNombre("Competencia 2");
        Competencia c3 = new Competencia();
        c3.setId(3L);
        c3.setCodigo("COMP-003");
        c3.setTipoCompetencia(TipoCompetencia.TRANSVERSAL);
        c3.setNombre("Competencia 3");
        Competencia c4 = new Competencia();
        c4.setId(4L);
        c4.setCodigo("COMP-004");
        c4.setTipoCompetencia(TipoCompetencia.TECNICA);
        c4.setNombre("Competencia 4");
        Competencia c5 = new Competencia();
        c5.setId(5L);
        c5.setCodigo("COMP-005");
        c5.setTipoCompetencia(TipoCompetencia.TECNICA);
        c5.setNombre("Competencia 5");

        List<Competencia> todaslascompetencias = List.of(c1,c2,c3,c4,c5);
        List<Competencia> competenciasTecnicas = List.of(c2,c4,c5);
        List<Competencia> competenciasTransversales = List.of(c1,c3);


        given(competenciaRepository.findByTipoCompetencia(TipoCompetencia.TECNICA)).willReturn(competenciasTecnicas);


        List<Competencia> resultado = obtenerCompetenciaUseCase.obtenerPorTipoCompetencia(TipoCompetencia.TECNICA);

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals(TipoCompetencia.TECNICA, resultado.get(0).getTipoCompetencia());
        then(competenciaRepository).should().findByTipoCompetencia(TipoCompetencia.TECNICA);

        given(competenciaRepository.findByTipoCompetencia(TipoCompetencia.TRANSVERSAL)).willReturn(competenciasTransversales);
        List<Competencia> resultado2 = obtenerCompetenciaUseCase.obtenerPorTipoCompetencia(TipoCompetencia.TRANSVERSAL);

        assertNotNull(resultado2);
        assertEquals(2, resultado2.size());
        assertEquals(TipoCompetencia.TRANSVERSAL, resultado2.get(0).getTipoCompetencia());
        then(competenciaRepository).should().findByTipoCompetencia(TipoCompetencia.TRANSVERSAL);

        System.out.println("Competencias:  " + todaslascompetencias.size());
        todaslascompetencias.forEach(c-> System.out.println("Id" + c.getId()  +  " Codigo " + c.getCodigo() + " Nombre " + c.getNombre() + " Tipo de competencia "  + c.getTipoCompetencia()));

        System.out.println("Competencias por tipo tecnica:   " + resultado.size());
        resultado.forEach(c-> System.out.println("Id" + c.getId()  +  " Codigo " + c.getCodigo() + " Nombre " + c.getNombre() + " Tipo de competencia "  + c.getTipoCompetencia()));

        System.out.println("Competencias por tipo transversal:   " + resultado2.size());
        resultado2.forEach(c-> System.out.println("Id " + c.getId() + "Codigo " + c.getCodigo() + " Nombre " + c.getNombre() + "Tipo de competencia " + c.getTipoCompetencia()) );


    }


}
