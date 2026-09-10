package com.caeproject.cae.application.usecases.programacionacademica;

import com.caeproject.cae.domain.ports.exceptions.disenocurricularexception.DiseñoCurricularNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.rapexception.RapNoEncontradoException;
import com.caeproject.cae.domain.ports.in.asignarinstructor.SugerirInstructorInputPort;
import com.caeproject.cae.domain.ports.in.programacionacademica.AutoProgramacionAcademicaInputPort;
import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.out.*;

import java.util.ArrayList;
import java.util.List;

public class AutoProgramarUseCase implements AutoProgramacionAcademicaInputPort {

    private final SugerirInstructorInputPort sugerirInstructorInputPort;
    private final ProgramacionAcademicaRepository programacionAcademicaRepository;
    private final DisponibilidadInstructorRepository disponibilidadInstructorRepository;
    private final DiseñoCurricularRepository disenoCurricularRepository;
    private final RapRepository rapRepository;
    private final FichaRepository fichaRepository;

    public AutoProgramarUseCase(SugerirInstructorInputPort sugerirInstructorInputPort, ProgramacionAcademicaRepository programacionAcademicaRepository, DisponibilidadInstructorRepository disponibilidadInstructorRepository, DiseñoCurricularRepository disenoCurricularRepository, RapRepository rapRepository, FichaRepository fichaRepository) {
        this.sugerirInstructorInputPort = sugerirInstructorInputPort;
        this.programacionAcademicaRepository = programacionAcademicaRepository;
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
        this.disenoCurricularRepository = disenoCurricularRepository;
        this.rapRepository = rapRepository;
        this.fichaRepository = fichaRepository;
    }

    @Override
    public List<ProgramacionAcademica> autoprogramarficha(Long fichaId, Long trimestreId) {
        Ficha ficha = fichaRepository.findById(fichaId)
                .orElseThrow(()-> new FichaNoEncontradaException(fichaId));
        Long programaId = ficha.getProgramaId();

        List<DiseñoCurricular> mallas = disenoCurricularRepository
                .findByProgramaIdAndTrimestreId(programaId, trimestreId);

        if (mallas == null || mallas.isEmpty()){
            throw new DiseñoCurricularNoEncontradoException(programaId,trimestreId);
        }

        // 1. Recalcular la carga horaria acumulada del Trimestre para no duplicar horas
        List<ProgramacionAcademica> programacionesPreviasTrimestre = programacionAcademicaRepository.findByTrimestre(trimestreId);
        List<DisponibilidadInstructor> disponibilidades = disponibilidadInstructorRepository.findAll();

        for (DisponibilidadInstructor disp : disponibilidades) {
            long horasAsignadasEnEsteTrimestre = 0L;
            for (ProgramacionAcademica pa : programacionesPreviasTrimestre) {
                if (pa.getUsuarioId() != null && pa.getUsuarioId().equals(disp.getUsuarioId())) {
                    DiseñoCurricular dis = disenoCurricularRepository
                            .findByProgramaIdAndTrimestreId(programaId, trimestreId).stream()
                            .filter(d -> d.getRapId().equals(pa.getRapId()))
                            .findFirst().orElse(null);
                    if (dis != null) {
                        horasAsignadasEnEsteTrimestre += dis.getHoraspresenciales();
                    }
                }
            }
            disp.setHorasAsignadas(horasAsignadasEnEsteTrimestre);
            disponibilidadInstructorRepository.saveDisponibilidad(disp);
        }

        List<ProgramacionAcademica> programacionesCreadas = new ArrayList<>();



        for (DiseñoCurricular diseño : mallas) {
            // Evitar duplicados: Si el RAP ya está programado en esta Ficha y Trimestre, omitir reinserción
            boolean yaProgramado = programacionAcademicaRepository
                    .existsByRapIdAndFichaIdAndTrimestreId(diseño.getRapId(), fichaId, trimestreId);

            if (yaProgramado) {
                continue;
            }

            Rap rap = rapRepository.findById(diseño.getRapId())
                    .orElseThrow(() -> new RapNoEncontradoException(diseño.getRapId()));

            Long competenciaId = rap.getCompetenciaId();
            Long horasRequeridas = Long.valueOf(diseño.getHoraspresenciales());

            List<DisponibilidadInstructor> candidatos = sugerirInstructorInputPort
                    .sugerirInstructores(competenciaId, fichaId, horasRequeridas);

            if (!candidatos.isEmpty()) {
                DisponibilidadInstructor ganador = candidatos.get(0);

                List<ProgramacionAcademica> asignacionesPreviasInstructor = programacionAcademicaRepository
                        .findByUserIdAndTrimestreId(ganador.getUsuarioId(), trimestreId);

                boolean existeCruceFicha = asignacionesPreviasInstructor.stream()
                        .anyMatch(p -> p.getFichaId() != null && p.getFichaId().equals(fichaId) && p.getRapId().equals(rap.getId()));

                if (existeCruceFicha) {
                    throw new com.caeproject.cae.domain.ports.exceptions.asignacionexceptions.CruceHorarioException(
                            "Conflicto de cruce: El instructor ID#" + ganador.getUsuarioId() + " ya tiene asignada la Ficha #" + ficha.getCodigoFicha() + " para el RAP #" + rap.getId(),
                            ficha.getCodigoFicha()
                    );
                }

                ProgramacionAcademica programacion = new ProgramacionAcademica();
                programacion.setTrimestreId(trimestreId);
                programacion.setRapId(rap.getId());
                programacion.setUsuarioId(ganador.getUsuarioId());
                programacion.setProgramaId(ficha.getProgramaId());
                programacion.setFichaId(fichaId);

                ProgramacionAcademica guardada = programacionAcademicaRepository.saveProgramacion(programacion);
                programacionesCreadas.add(guardada);

                ganador.comprometerHoras(horasRequeridas);
                disponibilidadInstructorRepository.saveDisponibilidad(ganador);
            }
        }

        return programacionesCreadas;
    }
}
