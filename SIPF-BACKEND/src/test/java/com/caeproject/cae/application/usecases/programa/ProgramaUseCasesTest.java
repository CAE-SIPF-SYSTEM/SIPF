package com.caeproject.cae.application.usecases.programa;

import com.caeproject.cae.application.usecases.programa.commands.CrearProgramaCommand;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaDuplicadoException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.model.Departamento;
import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.domain.ports.out.UbicacionRepository;
import com.caeproject.cae.infraestructure.adapter.out.ubicacion.MunicipioJPARepository;
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
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProgramaUseCasesTest {

    @Mock
    private ProgramaRepository programaRepository;
    @Mock
    private UbicacionRepository ubicacionRepository;

    @Mock
    private CrearProgramaCommand command;

    @InjectMocks
    private CrearProgramaUseCase crearProgramaUseCase;

    @InjectMocks
    private EliminarProgramaUseCase eliminarProgramaUseCase;

    @InjectMocks
    private ListarProgramaUseCase listarProgramaUseCase;

    @InjectMocks
    private ObtenerProgramaUseCase obtenerProgramaUseCase;


    @Test
    @DisplayName("Intento de creacion nombre duplicado")
    void crear_programa_nombreDuplicado(){
        CrearProgramaCommand command = new CrearProgramaCommand();

        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());

        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);

        d.getMunicipios().add(m);

        command.setDuracionpracticas(6);
        command.setNombre("ADSO");
        command.setMunicipio(m);
        command.setNivelFormacion(NivelFormacion.TECNOLOGO);
        command.setJornada(Jornada.MAÑANA);

        given(ubicacionRepository.obtenerMunicipioPorId(1L)).willReturn(Optional.of(m));
        given(programaRepository.existByName(command.getNombre())).willReturn(true);
        assertThrows(ProgramaDuplicadoException.class, () -> crearProgramaUseCase.crearPrograma(command));

        then(programaRepository).should(never()).savePrograma(any());
        System.out.println("Se lanza ProgramaDuplicadoException correctamente al intentar crear un programa con nombre duplicado.");
        System.out.println(ProgramaDuplicadoException.class);
    }



    @Test
    @DisplayName("Crear programa con nivel de formación nulo")
    void crear_programa_nivelFormacionNulo() {
        CrearProgramaCommand command = new CrearProgramaCommand();
        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());

        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);

        d.getMunicipios().add(m);


        command.setDuracionpracticas(6);
        command.setNombre("ADSO");
        command.setMunicipio(m);
        command.setNivelFormacion(null);
        command.setJornada(Jornada.MAÑANA);

        assertThrows(IllegalArgumentException.class, () -> crearProgramaUseCase.crearPrograma(command));

        then(programaRepository).should(never()).savePrograma(any());
        System.out.println("Se lanza la excepción correctamente al intentar crear un programa con nivel de formación nulo.");
    }

    @Test
    @DisplayName("Crear programa con duración de prácticas nula")
    void crear_programa_duracionPracticasNula() {
        CrearProgramaCommand command = new CrearProgramaCommand();
        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());

        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);

        d.getMunicipios().add(m);


        command.setDuracionpracticas(null);
        command.setNombre("ADSO");
        command.setMunicipio(m);
        command.setNivelFormacion(NivelFormacion.TECNOLOGO);
        command.setJornada(Jornada.MAÑANA);

        assertThrows(IllegalArgumentException.class, () -> crearProgramaUseCase.crearPrograma(command));

        then(programaRepository).should(never()).savePrograma(any());
        System.out.println("Se lanza la excepción correctamente al intentar crear un programa con duración de prácticas nula.");
    }
    
    @Test
    @DisplayName("Crear programa correctamente")
    void crear_programa_correctamente(){

        CrearProgramaCommand command = new CrearProgramaCommand();

        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());

        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);

        d.getMunicipios().add(m);

        command.setDuracionpracticas(6);
        command.setNombre("ADSO");
        command.setMunicipio(m);
        command.setNivelFormacion(NivelFormacion.TECNOLOGO);
        command.setJornada(Jornada.MAÑANA);

        Programa programaGuardado = new Programa();
        programaGuardado.setId(1L);
        programaGuardado.setNombre("ADSO");
        programaGuardado.setDuracionpracticas(6);
        programaGuardado.setNivelFormacion(NivelFormacion.TECNOLOGO);
        programaGuardado.setJornada(Jornada.MAÑANA);
        programaGuardado.setMunicipio(m);

        given(ubicacionRepository.obtenerMunicipioPorId(1L)).willReturn(Optional.of(m));
        given(programaRepository.existByName(command.getNombre())).willReturn(false);
        given(programaRepository.savePrograma(any(Programa.class))).willReturn(programaGuardado);

        Programa resultado = crearProgramaUseCase.crearPrograma(command);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("ADSO", resultado.getNombre());
        assertEquals(6, resultado.getDuracionpracticas());
        assertEquals(NivelFormacion.TECNOLOGO, resultado.getNivelFormacion());
        assertEquals(Jornada.MAÑANA, resultado.getJornada());
        assertEquals(m, resultado.getMunicipio());
        assertEquals(d, resultado.getMunicipio().getDepartamento());

        then(programaRepository).should().savePrograma(any(Programa.class));

       System.out.println("Se ha creado correctamente el programa");
       System.out.println("ID" +
               "Id " + resultado.getId() + " Nombre " + resultado.getNombre() +
               " Jornada  " + resultado.getJornada() +
               " Nivel de formacion " + resultado.getNivelFormacion()
               + " Municipio " + resultado.getMunicipio().getNombre()
               + " Duracion de practicas " + resultado.getDuracionpracticas() );
    }

    @Test
    @DisplayName("Inentar eliminar programa inexistente")
    void intentar_eliminar_programa_no_encontrado(){
        given(programaRepository.findById(99L)).willReturn(Optional.empty());
        assertThrows(ProgramaNoEncontradoException.class, () -> eliminarProgramaUseCase.eliminarPrograma(99L));
        then(programaRepository).should(never()).deletePrograma(any());
        System.out.println("Se lanza la excepción correctamente al intentar eliminar un programa inexistente.");

    }

    @Test
    @DisplayName("Eliminar programa correctamente")
    void eliminar_programa_correctamente(){
        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());

        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);

        Programa p = new Programa();

        p.setId(1L);
        p.setNombre("ADSO");
        p.setDuracionpracticas(6);
        p.setNivelFormacion(NivelFormacion.TECNOLOGO);
        p.setJornada(Jornada.MAÑANA);
        p.setMunicipio(m);

        given(programaRepository.findById(1L)).willReturn(Optional.of(p));

        System.out.println("Se ha eliminado correctamente el programa " +
                p.getId() + " " +
                p.getNombre() + " " +
                " Duracion practicas " + p.getDuracionpracticas() + " " +
                p.getMunicipio().getNombre() + " " +
                p.getNivelFormacion() + " " +
                p.getJornada());
        eliminarProgramaUseCase.eliminarPrograma(p.getId());
        then(programaRepository).should().deletePrograma(p.getId());

    }

    @Test
    @DisplayName("Listar todos los programas")
    void listar_programas(){

        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());

        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);

        Programa p = new Programa();

        p.setId(1L);
        p.setNombre("ADSO");
        p.setDuracionpracticas(6);
        p.setNivelFormacion(NivelFormacion.TECNOLOGO);
        p.setJornada(Jornada.MAÑANA);
        p.setMunicipio(m);

        Programa p2 = new Programa();
        p2.setId(2L);
        p2.setNombre("Cocina");
        p2.setDuracionpracticas(6);
        p2.setNivelFormacion(NivelFormacion.TECNOLOGO);
        p2.setJornada(Jornada.MAÑANA);
        p2.setMunicipio(m);

        Programa p3 = new Programa();
        p3.setId(3L);
        p3.setNombre("Panaderia");
        p3.setDuracionpracticas(6);
        p3.setNivelFormacion(NivelFormacion.TECNICO);
        p3.setJornada(Jornada.MAÑANA);
        p3.setMunicipio(m);

        List<Programa> programas = new ArrayList<>();
        programas.add(p);
        programas.add(p2);
        programas.add(p3);

        given(programaRepository.findAll()).willReturn(programas);

        List<Programa> resultado = listarProgramaUseCase.listarProgramas();

        then(programaRepository).should().findAll();

        System.out.println("Tamaño de la lista de programas " + resultado.size());
        resultado.forEach(pr -> System.out.println(
                "Id " + pr.getId()  + " " +
                " Nombre " + pr.getNombre() + " " +
                " Duracion practicas " + pr.getDuracionpracticas() + " " +
                " Nivel de formación " + pr.getNivelFormacion() + " " +
                " Jornada " + pr.getJornada() + " " +
                " Municipio " + pr.getMunicipio().getNombre()
        ));
    }

    @Test
    @DisplayName("Obtener lista vacia de programas")
    void lista_programas_vacia() {

        List<Programa> programas = new ArrayList<>();

        given(programaRepository.findAll()).willReturn(programas);

        List<Programa> resultado = listarProgramaUseCase.listarProgramas();

        then(programaRepository).should().findAll();

        System.out.println("Tamaño de la lista de programas " + resultado.size());
        System.out.println(resultado);

    }

    @Test
    @DisplayName("Obtener programa por jornada")
    void obtener_programa_por_jornada(){

        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());

        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);

        Programa p = new Programa();

        p.setId(1L);
        p.setNombre("ADSO");
        p.setDuracionpracticas(6);
        p.setNivelFormacion(NivelFormacion.TECNOLOGO);
        p.setJornada(Jornada.MAÑANA);
        p.setMunicipio(m);

        Programa p2 = new Programa();
        p2.setId(2L);
        p2.setNombre("Cocina");
        p2.setDuracionpracticas(6);
        p2.setNivelFormacion(NivelFormacion.TECNOLOGO);
        p2.setJornada(Jornada.MAÑANA);
        p2.setMunicipio(m);

        Programa p3 = new Programa();
        p3.setId(3L);
        p3.setNombre("Panaderia");
        p3.setDuracionpracticas(6);
        p3.setNivelFormacion(NivelFormacion.TECNICO);
        p3.setJornada(Jornada.MAÑANA);
        p3.setMunicipio(m);

        Programa p4 = new Programa();
        p4.setId(4L);
        p4.setNombre("Repuestos");
        p4.setDuracionpracticas(6);
        p4.setNivelFormacion(NivelFormacion.TECNICO);
        p4.setJornada(Jornada.TARDE);
        p4.setMunicipio(m);

        Programa p5 = new Programa();
        p5.setId(5L);
        p5.setNombre("Motores diesel");
        p5.setDuracionpracticas(6);
        p5.setNivelFormacion(NivelFormacion.TECNOLOGO);
        p5.setJornada(Jornada.TARDE);
        p5.setMunicipio(m);

        List<Programa> programasmañana = List.of(p,p2,p3);
        List<Programa> programas = List.of(p,p2,p3,p4,p5);


        given(programaRepository.findByJornada(Jornada.MAÑANA)).willReturn(programasmañana);

        List<Programa> resultado = obtenerProgramaUseCase.obtenerProgramaJornada(Jornada.MAÑANA);
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals(Jornada.MAÑANA, resultado.get(0).getJornada());

        then(programaRepository).should().findByJornada(Jornada.MAÑANA);

        System.out.println("Numero de programas " + programas.size());
        programas.forEach(px -> System.out.println(
                "Id " + px.getId()  + " " +
                        " Nombre " + px.getNombre() + " " +
                        " Duracion practicas " + px.getDuracionpracticas() + " " +
                        " Nivel de formación " + px.getNivelFormacion() + " " +
                        " Jornada " + px.getJornada() + " " +
                        " Municipio " + px.getMunicipio().getNombre()
        ));

        System.out.println("Numero de programas jornada mañana " +   programasmañana.size());
        programasmañana.forEach(ps -> System.out.println("Id " + ps.getId()  + " " +
                " Nombre " + ps.getNombre() + " " +
                " Duracion practicas " + ps.getDuracionpracticas() + " " +
                " Nivel de formación " + ps.getNivelFormacion() + " " +
                " Jornada " + ps.getJornada() + " " +
                " Municipio " + ps.getMunicipio().getNombre()));
    }

    @Test
    @DisplayName("Obtener programas por nivel de formación")
    void obtener_programa_por_nivelFormacion() {
        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());
        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);
        d.getMunicipios().add(m);
        Programa p1 = new Programa();
        p1.setId(1L);
        p1.setNombre("ADSO");
        p1.setDuracionpracticas(6);
        p1.setNivelFormacion(NivelFormacion.TECNOLOGO);
        p1.setJornada(Jornada.MAÑANA);
        p1.setMunicipio(m);
        Programa p2 = new Programa();
        p2.setId(2L);
        p2.setNombre("Cocina");
        p2.setDuracionpracticas(6);
        p2.setNivelFormacion(NivelFormacion.TECNOLOGO);
        p2.setJornada(Jornada.MAÑANA);
        p2.setMunicipio(m);
        Programa p3 = new Programa();
        p3.setId(3L);
        p3.setNombre("Panaderia");
        p3.setDuracionpracticas(6);
        p3.setNivelFormacion(NivelFormacion.TECNICO);
        p3.setJornada(Jornada.MAÑANA);
        p3.setMunicipio(m);

        Programa p4 = new Programa();
        p4.setId(4L);
        p4.setNombre("Repuestos");
        p4.setDuracionpracticas(6);
        p4.setNivelFormacion(NivelFormacion.TECNICO);
        p4.setJornada(Jornada.TARDE);
        p4.setMunicipio(m);
        // 5. Programa Operario
        Programa p5 = new Programa();
        p5.setId(5L);
        p5.setNombre("Mantenimiento de Instalaciones");
        p5.setDuracionpracticas(6);
        p5.setNivelFormacion(NivelFormacion.AUXILIAR);
        p5.setJornada(Jornada.MAÑANA);
        p5.setMunicipio(m);

        List<Programa> todosLosProgramas = List.of(p1, p2, p3, p4, p5);

        List<Programa> programasTecnologos = List.of(p1, p2);
        given(programaRepository.findByNiveldeFormacion(NivelFormacion.TECNOLOGO)).willReturn(programasTecnologos);
        List<Programa> resultado = obtenerProgramaUseCase.obtenerProgramaNivelFormacion(NivelFormacion.TECNOLOGO);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(NivelFormacion.TECNOLOGO, resultado.get(0).getNivelFormacion());
        then(programaRepository).should().findByNiveldeFormacion(NivelFormacion.TECNOLOGO);

        System.out.println("TOTAL DE PROGRAMAS " + todosLosProgramas.size());
        todosLosProgramas.forEach(px -> System.out.println(
                "Id " + px.getId() + " " +
                        " Nombre " + px.getNombre() + " " +
                        " Nivel de formación " + px.getNivelFormacion() + " " +
                        " Jornada " + px.getJornada() + " " +
                        " Municipio " + px.getMunicipio().getNombre()
        ));

        System.out.println("NUMERO DE PROGRAMAS POR NIVEL TECNOLOGO " + resultado.size());
        resultado.forEach(ps -> System.out.println(
                "Id " + ps.getId() + " " +
                        " Nombre " + ps.getNombre() + " " +
                        " Nivel de formacion " + ps.getNivelFormacion() + " " +
                        " Jornada " + ps.getJornada() + " " +
                        " Municipio " + ps.getMunicipio().getNombre()
        ));
    }

}
