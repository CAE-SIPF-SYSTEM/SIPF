package com.caeproject.cae.application.usecases.programacionacademica;

import com.caeproject.cae.domain.ports.exceptions.asignacionexceptions.CruceHorarioException;
import com.caeproject.cae.domain.ports.in.asignarinstructor.SugerirInstructorInputPort;
import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.out.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutoProgramarCruceHorarioTest {

    private SugerirInstructorInputPort sugerirInstructorInputPort;
    private ProgramacionAcademicaRepository programacionAcademicaRepository;
    private DisponibilidadInstructorRepository disponibilidadInstructorRepository;
    private DiseñoCurricularRepository disenoCurricularRepository;
    private RapRepository rapRepository;
    private FichaRepository fichaRepository;
    private AutoProgramarUseCase autoProgramarUseCase;

    @BeforeEach
    void setUp() {
        sugerirInstructorInputPort = mock(SugerirInstructorInputPort.class);
        programacionAcademicaRepository = mock(ProgramacionAcademicaRepository.class);
        disponibilidadInstructorRepository = mock(DisponibilidadInstructorRepository.class);
        disenoCurricularRepository = mock(DiseñoCurricularRepository.class);
        rapRepository = mock(RapRepository.class);
        fichaRepository = mock(FichaRepository.class);

        autoProgramarUseCase = new AutoProgramarUseCase(
                sugerirInstructorInputPort,
                programacionAcademicaRepository,
                disponibilidadInstructorRepository,
                disenoCurricularRepository,
                rapRepository,
                fichaRepository
        );
    }

    @Test
    void debeLanzarCruceHorarioExceptionSiInstructorYaTieneElMismoRapYFichaAsignado() {
        // Arrange
        Long fichaId = 10L;
        Long trimestreId = 1L;
        Long programaId = 100L;
        Long rapId = 50L;
        Long usuarioId = 5L;

        Ficha ficha = new Ficha();
        ficha.setId(fichaId);
        ficha.setCodigoFicha("228106-G1");
        ficha.setProgramaId(programaId);

        DiseñoCurricular diseno = new DiseñoCurricular();
        diseno.setRapId(rapId);
        diseno.setHoraspresenciales(40);

        Rap rap = new Rap();
        rap.setId(rapId);
        rap.setCompetenciaId(30L);

        DisponibilidadInstructor instructorCandidato = new DisponibilidadInstructor();
        instructorCandidato.setUsuarioId(usuarioId);

        ProgramacionAcademica asignacionPrevia = new ProgramacionAcademica();
        asignacionPrevia.setFichaId(fichaId);
        asignacionPrevia.setRapId(rapId);
        asignacionPrevia.setUsuarioId(usuarioId);
        asignacionPrevia.setTrimestreId(trimestreId);

        when(fichaRepository.findById(fichaId)).thenReturn(Optional.of(ficha));
        when(disenoCurricularRepository.findByProgramaIdAndTrimestreId(programaId, trimestreId))
                .thenReturn(List.of(diseno));
        when(rapRepository.findById(rapId)).thenReturn(Optional.of(rap));
        when(sugerirInstructorInputPort.sugerirInstructores(30L, fichaId, 40L))
                .thenReturn(List.of(instructorCandidato));

        // Simular que el instructor ya tiene esta asignación previa en el trimestre
        when(programacionAcademicaRepository.findByUserIdAndTrimestreId(usuarioId, trimestreId))
                .thenReturn(List.of(asignacionPrevia));

        // Act & Assert
        CruceHorarioException exception = assertThrows(CruceHorarioException.class, () -> {
            autoProgramarUseCase.autoprogramarficha(fichaId, trimestreId);
        });

        assertTrue(exception.getMessage().contains("228106-G1"));
        assertEquals("228106-G1", exception.getFichaCodigo());
    }
}
