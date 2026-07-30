package com.caeproject.cae.application.usecases.programacionacademica;

import com.caeproject.cae.domain.ports.in.programacionacademica.AutoProgramacionInputPort;
import com.caeproject.cae.domain.ports.in.asignarinstructor.SugerirInstructorInputPort;
import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.out.*;

import java.util.ArrayList;
import java.util.List;

public class AutoProgramarTrimestreUseCase implements AutoProgramacionInputPort {

    private final SugerirInstructorInputPort sugerirInstructorInputPort;
    private final ProgramacionAcademicaRepository programacionAcademicaRepository;
    private final DisponibilidadInstructorRepository disponibilidadInstructorRepository;
    private final DiseñoCurricularRepository disenoCurricularRepository;
    private final RapRepository rapRepository;

    public AutoProgramarTrimestreUseCase(
            SugerirInstructorInputPort sugerirInstructorInputPort,
            ProgramacionAcademicaRepository programacionAcademicaRepository,
            DisponibilidadInstructorRepository disponibilidadInstructorRepository,
            DiseñoCurricularRepository disenoCurricularRepository,
            RapRepository rapRepository) {

        this.sugerirInstructorInputPort = sugerirInstructorInputPort;
        this.programacionAcademicaRepository = programacionAcademicaRepository;
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
        this.disenoCurricularRepository = disenoCurricularRepository;
        this.rapRepository = rapRepository;
    }

    @Override
    public List<ProgramacionAcademica> autoprogramar(Long programaId, Long trimestreId) {

        List<ProgramacionAcademica> programacionesCreadas = new ArrayList<>();

        List<DiseñoCurricular> mallas = disenoCurricularRepository
                .findByProgramaIdAndTrimestreId(programaId, trimestreId);

        for (DiseñoCurricular diseño : mallas) {

            Rap rap = rapRepository.findById(diseño.getRapId())
                    .orElseThrow(() -> new RuntimeException("No se encontró el RAP con id " + diseño.getRapId()));

            Long competenciaId = rap.getCompetenciaId();
            Long horasRequeridas = Long.valueOf(diseño.getHoraspresenciales());

            List<DisponibilidadInstructor> candidatos = sugerirInstructorInputPort
                    .sugerirInstructores(competenciaId, programaId, horasRequeridas);

            if (!candidatos.isEmpty()) {
                DisponibilidadInstructor ganador = candidatos.get(0);

                ProgramacionAcademica programacion = new ProgramacionAcademica();
                programacion.setTrimestreId(trimestreId);
                programacion.setRapId(rap.getId());
                programacion.setUsuarioId(ganador.getUsuarioId());
                programacion.setProgramaId(programaId);

                ProgramacionAcademica guardada = programacionAcademicaRepository.saveProgramacion(programacion);
                programacionesCreadas.add(guardada);

                ganador.comprometerHoras(horasRequeridas);
                disponibilidadInstructorRepository.saveDisponibilidad(ganador);
            }
        }

        return programacionesCreadas;
    }
}
