package com.caeproject.cae.application.usecases.programacionacademica.autoprogramacion;
import com.caeproject.cae.application.usecases.asignacioninstructor.SugerirInstructorUseCase;
import com.caeproject.cae.application.usecases.programacionacademica.AutoProgramarUseCase;
import com.caeproject.cae.domain.ports.exceptions.asignacionexceptions.CruceHorarioException;
import com.caeproject.cae.domain.ports.exceptions.disenocurricularexception.DiseñoCurricularNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaDuplicadaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.rapexception.RapNoEncontradoException;
import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.out.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class AutoProgramacionUseCaseTest {

    @InjectMocks
    private AutoProgramarUseCase programarUseCase;

    @Mock
    private ProgramacionAcademicaRepository programacionAcademicaRepository;

    @Mock
    private DiseñoCurricularRepository disenocurricular;

    @Mock
    private DisponibilidadInstructorRepository disponibilidad;


    @Mock
    private RapRepository rapRepository;

    @Mock
    private SugerirInstructorUseCase sugerirInstructorUseCase;

    @Mock
    private FichaRepository fichaRepository;



    @Test
    @DisplayName("Ejecutar AutoProgramacionUseCase ficha no encontrada")
    void ejecutar_autoprogramacion_fichaNoencontrada(){

        Ficha ficha1 = new Ficha();
        ficha1.setId(1L);
        ficha1.setProgramaId(2L);
        ficha1.setCodigoFicha("2996315");
        ficha1.setFechaInicio(new Date());
        ficha1.setFechaFin(new Date());

        Trimestre trimestre1 = new Trimestre();
        trimestre1.setId(1L);
        trimestre1.setFichaId(1L);
        trimestre1.setAnio(2026);
        trimestre1.setNumeroTrimestre(1);

        given(fichaRepository.findById(ficha1.getId())).willReturn(Optional.empty());

        assertThrows(FichaNoEncontradaException.class, ()-> programarUseCase.autoprogramarficha(ficha1.getId(),trimestre1.getId()));
        then(programacionAcademicaRepository).should(never()).saveProgramacion(any());

        System.out.println("Se lanza FichaNoEncontradaExcepction debido que la ficha buscada era la ficha numero 2 y no fue encontrada");
        System.out.println(FichaDuplicadaException.class);
        System.out.println("Datos ingresados Ficha:  " + ficha1.getId() + " " + "Codigo Ficha " + ficha1.getCodigoFicha());


    }

    @Test
    @DisplayName("ejecutar autoprogramacion sin diseño curricular")
    void ejecutar_autoprogramacion_sindisenocurricular(){

        Ficha ficha1 = new Ficha();
        ficha1.setProgramaId(1L);
        ficha1.setCodigoFicha("2996315");
        ficha1.setId(2L);

        Trimestre t1 = new Trimestre();
        t1.setId(1L);

        given(fichaRepository.findById(ficha1.getId())).willReturn(Optional.of(ficha1));
        given(disenocurricular.findByProgramaIdAndTrimestreId(1L, 1L)).willReturn(Collections.emptyList());

        assertThrows(DiseñoCurricularNoEncontradoException.class, ()-> programarUseCase.autoprogramarficha(ficha1.getId(), t1.getId()));
        then(programacionAcademicaRepository).should(never()).saveProgramacion(any());
        System.out.println("No se pudo autoprogramar esta ficha debido a que el programa no dispone de un diseño curricular dentro de el sistema");
    }


    @Test
    @DisplayName("ejecutar autoprogramacion rap no encontrado ")
    void ejecutar_autoprogramacion_rap_no_encontrado(){
        Ficha ficha1 = new Ficha();
        ficha1.setProgramaId(1L);
        ficha1.setCodigoFicha("2996315");
        ficha1.setId(2L);

        Trimestre t1 = new Trimestre();
        t1.setId(1L);

        Rap rap1 = new Rap();
        rap1.setId(1L);
        rap1.setDescripcion("02 DISEÑAR EL SOFTWARE DEACUERDO A REQUERIMIENTOS");
        given(fichaRepository.findById(ficha1.getId())).willReturn(Optional.of(ficha1));

        DiseñoCurricular diseno = new DiseñoCurricular();
        diseno.setRapId(rap1.getId());
        diseno.setHoraspresenciales(40);

        given(disenocurricular.findByProgramaIdAndTrimestreId(ficha1.getProgramaId(), t1.getId()))
                .willReturn(List.of(diseno));
        given(rapRepository.findById(rap1.getId())).willReturn(Optional.empty());

        assertThrows(RapNoEncontradoException.class, ()-> programarUseCase.autoprogramarficha(ficha1.getId(),t1.getId()));
        then(programacionAcademicaRepository).should(never()).saveProgramacion(any());
        System.out.println("No se pudo autoprogramar esta ficha a falta de un rap no encontrado");
        System.out.println("Ficha " + ficha1.getCodigoFicha() + " Programa " + ficha1.getProgramaId() + " RAP " + rap1.getDescripcion());
    }



    @Test
    @DisplayName("ejecutar autoprogramacion sin instructor candidatos")
    void ejecutar_autoprogramacion_sin_candidatos(){

        Ficha ficha1 = new Ficha();
        ficha1.setProgramaId(1L);
        ficha1.setCodigoFicha("2996315");
        ficha1.setId(2L);

        Trimestre t1 = new Trimestre();
        t1.setId(1L);

        given(fichaRepository.findById(ficha1.getId())).willReturn(Optional.of(ficha1));

        DiseñoCurricular diseno = new DiseñoCurricular();
        diseno.setRapId(1L);
        diseno.setHoraspresenciales(30);


        given(disenocurricular.findByProgramaIdAndTrimestreId(1L,1L)).willReturn(List.of(diseno));

        Rap rap1 = new Rap();
        rap1.setId(1L);
        rap1.setCompetenciaId(10L);

        given(rapRepository.findById(1L)).willReturn(Optional.of(rap1));
        given(sugerirInstructorUseCase.sugerirInstructores(any(),any(),any())).willReturn(Collections.emptyList());
        List<ProgramacionAcademica>resultado = programarUseCase.autoprogramarficha(ficha1.getId(),t1.getId());

        assertTrue(resultado.isEmpty(), "La lista de programaciones debe estar vacia");
        then(programacionAcademicaRepository).should(never()).saveProgramacion(any());

        System.out.println("No se autoprogramo esta ficha debeido a que no hay instructores para esta competencia asociado al rap");


    }

    @Test
    @DisplayName("Ejecutar autoprogramacion correctamente")
    void ejecutar_autoprogramacion_exitosa() {
        Ficha ficha = new Ficha();
        ficha.setId(2L);
        ficha.setProgramaId(1L);
        Trimestre trimestre = new Trimestre();
        trimestre.setId(1L);
        DiseñoCurricular diseno = new DiseñoCurricular();
        diseno.setRapId(10L);
        diseno.setHoraspresenciales(40);
        Rap rap = new Rap();
        rap.setId(10L);
        rap.setCompetenciaId(5L);
        DisponibilidadInstructor instructorCandidato = new DisponibilidadInstructor();
        instructorCandidato.setUsuarioId(99L);
        instructorCandidato.setHorasAsignadas(0L);
        instructorCandidato.setHorasMaximas(160L);
        ProgramacionAcademica guardada = new ProgramacionAcademica();
        guardada.setId(100L);
        given(fichaRepository.findById(ficha.getId())).willReturn(Optional.of(ficha));
        given(programacionAcademicaRepository.findByTrimestre(trimestre.getId())).willReturn(Collections.emptyList());
        given(disponibilidad.findAll()).willReturn(Collections.emptyList());
        given(disenocurricular.findByProgramaIdAndTrimestreId(ficha.getProgramaId(), trimestre.getId()))
                .willReturn(List.of(diseno));

        given(programacionAcademicaRepository.existsByRapIdAndFichaIdAndTrimestreId(
                diseno.getRapId(), ficha.getId(), trimestre.getId())).willReturn(false);
        given(rapRepository.findById(diseno.getRapId())).willReturn(Optional.of(rap));
        given(sugerirInstructorUseCase.sugerirInstructores(rap.getCompetenciaId(), ficha.getId(), 40L))
                .willReturn(List.of(instructorCandidato));
        given(programacionAcademicaRepository.findByUserIdAndTrimestreId(instructorCandidato.getUsuarioId(), trimestre.getId()))
                .willReturn(Collections.emptyList());
        given(programacionAcademicaRepository.saveProgramacion(any(ProgramacionAcademica.class)))
                .willReturn(guardada);
        List<ProgramacionAcademica> resultado = programarUseCase.autoprogramarficha(ficha.getId(), trimestre.getId());
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());

        then(programacionAcademicaRepository).should().saveProgramacion(any(ProgramacionAcademica.class));
        then(disponibilidad).should().saveDisponibilidad(instructorCandidato);
        ProgramacionAcademica p = resultado.get(0);
        System.out.println("Programacion academica autoprogramada correctamente ID: " + p.getId() +
                " RAP: " + p.getRapId() +
                " TRIMESTRE: " + p.getTrimestreId() +
                " USUARIO: " + p.getUsuarioId() +
                " FICHA: " + p.getFichaId());


}
    @Test
    @DisplayName("Omitir autoprogramacion por RAP ya programado")
    void ejecutar_autoprogramacion_rapYaProgramado() {
        Ficha ficha = new Ficha();
        ficha.setId(2L);
        ficha.setProgramaId(1L);
        Trimestre trimestre = new Trimestre();
        trimestre.setId(1L);
        DiseñoCurricular diseno = new DiseñoCurricular();
        diseno.setRapId(10L);
        given(fichaRepository.findById(ficha.getId())).willReturn(Optional.of(ficha));
        given(programacionAcademicaRepository.findByTrimestre(trimestre.getId())).willReturn(Collections.emptyList());
        given(disponibilidad.findAll()).willReturn(Collections.emptyList());

        given(disenocurricular.findByProgramaIdAndTrimestreId(ficha.getProgramaId(), trimestre.getId()))
                .willReturn(List.of(diseno));
        given(programacionAcademicaRepository.existsByRapIdAndFichaIdAndTrimestreId(
                diseno.getRapId(), ficha.getId(), trimestre.getId())).willReturn(true);
        List<ProgramacionAcademica> resultado = programarUseCase.autoprogramarficha(ficha.getId(), trimestre.getId());
        assertTrue(resultado.isEmpty());
        then(rapRepository).should(never()).findById(any());
        then(programacionAcademicaRepository).should(never()).saveProgramacion(any());

        System.out.println("Se omitió el RAP correctamente porque ya estaba programado.");
    }
    @Test
    @DisplayName("Excepcion por cruce de horario del instructor")
    void ejecutar_autoprogramacion_cruceHorarioException() {
        Ficha ficha = new Ficha();
        ficha.setId(2L);
        ficha.setProgramaId(1L);
        ficha.setCodigoFicha("12345");
        Trimestre trimestre = new Trimestre();
        trimestre.setId(1L);
        DiseñoCurricular diseno = new DiseñoCurricular();
        diseno.setRapId(10L);
        diseno.setHoraspresenciales(40);
        Rap rap = new Rap();
        rap.setId(10L);
        rap.setCompetenciaId(5L);
        DisponibilidadInstructor instructorCandidato = new DisponibilidadInstructor();
        instructorCandidato.setUsuarioId(99L);
        ProgramacionAcademica programacionPrevia = new ProgramacionAcademica();
        programacionPrevia.setFichaId(ficha.getId());
        programacionPrevia.setRapId(rap.getId());
        given(fichaRepository.findById(ficha.getId())).willReturn(Optional.of(ficha));
        given(programacionAcademicaRepository.findByTrimestre(trimestre.getId())).willReturn(Collections.emptyList());
        given(disponibilidad.findAll()).willReturn(Collections.emptyList());

        given(disenocurricular.findByProgramaIdAndTrimestreId(ficha.getProgramaId(), trimestre.getId()))
                .willReturn(List.of(diseno));

        given(programacionAcademicaRepository.existsByRapIdAndFichaIdAndTrimestreId(
                diseno.getRapId(), ficha.getId(), trimestre.getId())).willReturn(false);
        given(rapRepository.findById(diseno.getRapId())).willReturn(Optional.of(rap));
        given(sugerirInstructorUseCase.sugerirInstructores(rap.getCompetenciaId(), ficha.getId(), 40L))
                .willReturn(List.of(instructorCandidato));
        given(programacionAcademicaRepository.findByUserIdAndTrimestreId(instructorCandidato.getUsuarioId(), trimestre.getId()))
                .willReturn(List.of(programacionPrevia));
        assertThrows(CruceHorarioException.class,
                () -> programarUseCase.autoprogramarficha(ficha.getId(), trimestre.getId()));
        then(programacionAcademicaRepository).should(never()).saveProgramacion(any());

        System.out.println("Se detectó el cruce de horario y se lanzó CruceHorarioException exitosamente. " +
                "INSTRUCTOR CANDIDATO: " + instructorCandidato.getUsuarioId() +
                " RAP: " + rap.getId() +
                " FICHA: " + ficha.getCodigoFicha());
    }

}
