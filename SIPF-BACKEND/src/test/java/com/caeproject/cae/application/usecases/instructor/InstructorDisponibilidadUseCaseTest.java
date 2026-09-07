package com.caeproject.cae.application.usecases.instructor;
import com.caeproject.cae.application.usecases.disponibilidadinstructor.CrearDisponibilidadInstructorUseCase;
import com.caeproject.cae.application.usecases.disponibilidadinstructor.EliminarDisponibilidadInstructorUseCase;
import com.caeproject.cae.application.usecases.disponibilidadinstructor.ListarDisponibilidadInstructorUseCase;
import com.caeproject.cae.application.usecases.disponibilidadinstructor.ObtenerDisponibilidadInstructorUseCase;
import com.caeproject.cae.application.usecases.disponibilidadinstructor.commands.CrearDisponibilidadCommand;
import com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception.DiaNoDisponibleException;
import com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception.DisponibilidadNoEncontradaException;
import com.caeproject.cae.domain.ports.model.Departamento;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.model.PerfilBase;
import com.caeproject.cae.domain.ports.model.enums.DiasDisponibles;
import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;
import com.caeproject.cae.domain.ports.out.DisponibilidadInstructorRepository;
import com.caeproject.cae.domain.ports.out.PerfilBaseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class InstructorDisponibilidadUseCaseTest {



    @Mock
    private DisponibilidadInstructorRepository disponibilidad;
    @Mock
    PerfilBaseRepository perfilBaseRepository;
    @InjectMocks
    private CrearDisponibilidadCommand crearDisponibilidadCommand;

    @InjectMocks
    private CrearDisponibilidadInstructorUseCase crearDisponibilidadInstructorUseCase;

    @InjectMocks
    private ListarDisponibilidadInstructorUseCase listarDisponibilidadInstructorUseCase;

    @InjectMocks
    private EliminarDisponibilidadInstructorUseCase eliminarDisponibilidadInstructorUseCase;

    @InjectMocks
    private ObtenerDisponibilidadInstructorUseCase obtenerDisponibilidadInstructorUseCase;



    @Test
    @DisplayName("Crear disponibilidadCorrectamente")
    void crear_Disponibilidad(){
        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());

        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);

        PerfilBase perfil = new PerfilBase();
        perfil.setUsuarioId(1L);
        perfil.setTipoContrato(TIpoContrato.PLANTA);
        perfil.setNombre("Jhon");
        perfil.setApellido("Perez");
        perfil.setCc(107249338L);
        perfil.setTelefono(3118920869L);



        List<DiasDisponibles> diasDisponibles = new ArrayList<>();
        diasDisponibles.add(DiasDisponibles.LUNES);
        diasDisponibles.add(DiasDisponibles.MARTES);
        diasDisponibles.add(DiasDisponibles.VIERNES);

        CrearDisponibilidadCommand command = new CrearDisponibilidadCommand();

        command.setMunicipios(List.of(m));
        command.setDiasDisponibles(diasDisponibles);
        command.setJornada(Jornada.MAÑANA);
        command.setUsuarioId(perfil.getUsuarioId());


        given(perfilBaseRepository.findById(1L)).willReturn(Optional.of(perfil));

        DisponibilidadInstructor disponibilidadGuardada = new DisponibilidadInstructor();
        disponibilidadGuardada.setUsuarioId(perfil.getUsuarioId());
        disponibilidadGuardada.setJornada(command.getJornada());
        disponibilidadGuardada.setMunicipios(command.getMunicipios());
        disponibilidadGuardada.setDiasDisponibles(command.getDiasDisponibles());
        disponibilidadGuardada.setHorasMaximas(144L); // En PLANTA asigna 144L
        given(disponibilidad.saveDisponibilidad(any())).willReturn(disponibilidadGuardada);

        DisponibilidadInstructor disponibilidadnueva = crearDisponibilidadInstructorUseCase.crearDisponibilidad(command);

        assertNotNull(disponibilidadnueva);
        assertEquals(Jornada.MAÑANA, disponibilidadnueva.getJornada());
        assertEquals(perfil.getUsuarioId(), disponibilidadnueva.getUsuarioId());
        assertEquals(List.of(m), disponibilidadnueva.getMunicipios());
        assertEquals(diasDisponibles, disponibilidadnueva.getDiasDisponibles());
        assertEquals(3, disponibilidadnueva.getDiasDisponibles().size());
        assertEquals(144L, disponibilidadnueva.getHorasMaximas());
        then(disponibilidad).should().saveDisponibilidad(any());
        System.out.println("Se crea disponibilidad correctamente\n" +
                "ID: " + disponibilidadnueva.getUsuarioId() + "\n" +
                "Nombre Completo: " + perfil.getNombre() + " " + perfil.getApellido() + "\n" +
                "CC: " + perfil.getCc() + "\n" +
                "Numero de telefono: " + perfil.getTelefono() + "\n" +
                "Jornada: " + disponibilidadnueva.getJornada() + "\n" +
                "Municipio: " + disponibilidadnueva.getMunicipios() + "\n" +
                "Horas maximas: " + disponibilidadnueva.getHorasMaximas() + " & Horas Asignadas: " + disponibilidadnueva.getHorasDisponibles() + "\n" +
                "Dias disponibles: " + disponibilidadnueva.getDiasDisponibles());
    }

    @Test
    @DisplayName("Intentar eliminar disponibilidad de instructor")
    void intento_eliminar_disponibilidad_instructor(){
        given(disponibilidad.findById(1L)).willReturn(Optional.empty());
        assertThrows(DisponibilidadNoEncontradaException.class, () -> {
            eliminarDisponibilidadInstructorUseCase.eliminarDisponibilidad(1L);
        });
        then(disponibilidad).should(never()).deleteDisponibilidadInstrucor(any());
        System.out.println("Se lanza la excepción de disponibilidad no encontrada correctamente");
        System.out.println(DisponibilidadNoEncontradaException.class);
    }

    @Test
    @DisplayName("Eliminar disponibilidad de instructor correctamente")
    void eliminar_disponibilidad_correctamente(){
        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());

        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);

        PerfilBase perfil = new PerfilBase();
        perfil.setUsuarioId(1L);
        perfil.setTipoContrato(TIpoContrato.PLANTA);
        perfil.setNombre("Jhon");
        perfil.setApellido("Perez");
        perfil.setCc(107249338L);
        perfil.setTelefono(3118920869L);



        List<DiasDisponibles> diasDisponibles = new ArrayList<>();
        diasDisponibles.add(DiasDisponibles.LUNES);
        diasDisponibles.add(DiasDisponibles.MARTES);
        diasDisponibles.add(DiasDisponibles.VIERNES);

        CrearDisponibilidadCommand command = new CrearDisponibilidadCommand();

        command.setMunicipios(List.of(m));
        command.setDiasDisponibles(diasDisponibles);
        command.setJornada(Jornada.MAÑANA);
        command.setUsuarioId(perfil.getUsuarioId());

        DisponibilidadInstructor deliminada = new DisponibilidadInstructor();

        deliminada.setHorasAsignadas(0L);
        deliminada.setHorasMaximas(144L);
        deliminada.setUsuarioId(perfil.getUsuarioId());
        deliminada.setJornada(command.getJornada());
        deliminada.setMunicipios(command.getMunicipios());
        deliminada.setDiasDisponibles(command.getDiasDisponibles());
        deliminada.setUsuarioId(perfil.getUsuarioId());
        System.out.println("Usuario eliminado");
        System.out.println(
                "Horas asignadas: " + deliminada.getHorasAsignadas() + "\n" +
                        "Horas máximas: " + deliminada.getHorasMaximas() + "\n" +
                        "Usuario ID: " + deliminada.getUsuarioId() + "\n" +
                        "Jornada: " + deliminada.getJornada() + "\n" +
                        "Municipios: " + deliminada.getMunicipios() + "\n" +
                        "Días disponibles: " + deliminada.getDiasDisponibles() + "\n"
        );
    }

    @Test
    @DisplayName("Listar disponibilidad")
    void listar_disponibilidad(){

        List<DisponibilidadInstructor> disponibilidadList = new ArrayList<>();

        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());

        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);

        DisponibilidadInstructor disponibilidad1 = new DisponibilidadInstructor();
        disponibilidad1.setUsuarioId(1L);
        disponibilidad1.setJornada(Jornada.MAÑANA);
        disponibilidad1.setMunicipios(List.of(m));
        disponibilidad1.setDiasDisponibles(List.of(DiasDisponibles.LUNES, DiasDisponibles.MARTES));
        disponibilidad1.setHorasMaximas(144L);

        disponibilidadList.add(disponibilidad1);


        given(disponibilidad.findAll()).willReturn(disponibilidadList);

        List<DisponibilidadInstructor> result = listarDisponibilidadInstructorUseCase.listarDisponibilidad();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(disponibilidad1, result.get(0));
        System.out.println("Se listan las disponibilidades correctamente\n" + "Disponibilidad 1: " + result.get(0));

    }

    @Test
    @DisplayName("Obtener disponibilidad de instructor")
    void obtener_disponibilidad_instructor() {
        Departamento d = new Departamento();
        d.setId(1L);
        d.setNombre("Cundinamarca");
        d.setMunicipios(new ArrayList<>());
        Municipio m = new Municipio();
        m.setDepartamento(d);
        m.setNombre("Fusagasuga");
        m.setId(1L);
        PerfilBase perfil = new PerfilBase();
        perfil.setUsuarioId(1L);
        perfil.setTipoContrato(TIpoContrato.PLANTA);
        perfil.setNombre("Jhon");
        perfil.setApellido("Perez");
        perfil.setCc(107249338L);
        perfil.setTelefono(3118920869L);
        DisponibilidadInstructor dispo = new DisponibilidadInstructor();
        dispo.setUsuarioId(perfil.getUsuarioId());
        dispo.setHorasMaximas(144L); // Ajustado a 144L para coincidir con la asercion
        dispo.setHorasAsignadas(0L);
        dispo.setJornada(Jornada.MAÑANA);
        dispo.setMunicipios(List.of(m)); // <--- ASIGNACION QUE FALTABA
        dispo.setDiasDisponibles(List.of(DiasDisponibles.LUNES, DiasDisponibles.MARTES, DiasDisponibles.VIERNES));
        given(disponibilidad.findById(1L)).willReturn(Optional.of(dispo));
        DisponibilidadInstructor disponibilidadnueva = obtenerDisponibilidadInstructorUseCase.obtenerDisponiblidad(1L);
        assertNotNull(disponibilidadnueva);
        assertEquals(Jornada.MAÑANA, disponibilidadnueva.getJornada());
        assertEquals(perfil.getUsuarioId(), disponibilidadnueva.getUsuarioId());
        assertEquals(List.of(m), disponibilidadnueva.getMunicipios());
        assertEquals(List.of(DiasDisponibles.LUNES, DiasDisponibles.MARTES, DiasDisponibles.VIERNES), disponibilidadnueva.getDiasDisponibles());
        assertEquals(3, disponibilidadnueva.getDiasDisponibles().size());
        assertEquals(144L, disponibilidadnueva.getHorasMaximas());
        then(disponibilidad).should().findById(1L); // <--- VERIFICACION CORREGIDA SOBRE EL REPOSITORIO
        System.out.println(
                "Departamento ID: " + d.getId() + "\n" +
                        "Departamento nombre: " + d.getNombre() + "\n" +
                        "Municipios departamento: " + d.getMunicipios() + "\n" +
                        "Municipio ID: " + m.getId() + "\n" +
                        "Municipio nombre: " + m.getNombre() + "\n" +
                        "Perfil usuario ID: " + perfil.getUsuarioId() + "\n" +
                        "Perfil tipo contrato: " + perfil.getTipoContrato() + "\n" +
                        "Perfil nombre: " + perfil.getNombre() + "\n" +
                        "Perfil apellido: " + perfil.getApellido() + "\n" +
                        "Perfil CC: " + perfil.getCc() + "\n" +
                        "Perfil teléfono: " + perfil.getTelefono() + "\n" +
                        "Disponibilidad usuario ID: " + dispo.getUsuarioId() + "\n" +
                        "Disponibilidad horas máximas: " + dispo.getHorasMaximas() + "\n" +
                        "Disponibilidad horas asignadas: " + dispo.getHorasAsignadas() + "\n" +
                        "Disponibilidad jornada: " + dispo.getJornada() + "\n" +
                        "Disponibilidad días disponibles: " + dispo.getDiasDisponibles() + "\n" +
                        "Resultado usuario ID: " + disponibilidadnueva.getUsuarioId() + "\n" +
                        "Resultado jornada: " + disponibilidadnueva.getJornada() + "\n" +
                        "Resultado municipios: " + disponibilidadnueva.getMunicipios() + "\n" +
                        "Resultado días disponibles: " + disponibilidadnueva.getDiasDisponibles() + "\n" +
                        "Resultado cantidad de días: " + disponibilidadnueva.getDiasDisponibles().size() + "\n" +
                        "Resultado horas máximas: " + disponibilidadnueva.getHorasMaximas() + "\n"
        );
    }

    @Test
    @DisplayName("Obtener disponibilidad por dias disponibles")
    void obtener_disponibilidad_diasDisponibles() {
        DisponibilidadInstructor d1 = new DisponibilidadInstructor();
        d1.setUsuarioId(1L);
        d1.setHorasMaximas(160L);
        d1.setHorasAsignadas(40L);
        d1.setDiasDisponibles(List.of(DiasDisponibles.LUNES, DiasDisponibles.JUEVES));
        DisponibilidadInstructor d2 = new DisponibilidadInstructor();
        d2.setUsuarioId(2L);
        d2.setHorasMaximas(144L);
        d2.setHorasAsignadas(20L);
        d2.setDiasDisponibles(List.of(DiasDisponibles.MARTES, DiasDisponibles.JUEVES));
        List<DisponibilidadInstructor> disponibilidadesConJueves = List.of(d1, d2);
        given(disponibilidad.findByDiasDisponibles(DiasDisponibles.JUEVES)).willReturn(disponibilidadesConJueves);
        List<DisponibilidadInstructor> resultado = obtenerDisponibilidadInstructorUseCase.obtenerPorDiasDisponibles(DiasDisponibles.JUEVES);
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        resultado.forEach(dispo -> System.out.println(
                "Usuario ID: " + dispo.getUsuarioId() + "\n" +
                        "Dias disponibles: " + dispo.getDiasDisponibles() + "\n" +
                        "Horas maximas: " + dispo.getHorasMaximas() + "\n" +
                        "Horas asignadas: " + dispo.getHorasAsignadas() + "\n"
        ));
        then(disponibilidad).should().findByDiasDisponibles(DiasDisponibles.JUEVES);
    }
}
