package com.caeproject.cae.application.usecases.asignacioninstructor;

import com.caeproject.cae.domain.ports.in.asignarinstructor.SugerirInstructorInputPort;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.out.CompetenciaEspecialidadRepository;
import com.caeproject.cae.domain.ports.out.DisponibilidadInstructorRepository;
import com.caeproject.cae.domain.ports.out.InstructorEspecialidadRepository;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.domain.ports.service.ValidarElegibilidadInstructor;

import java.util.Comparator;
import java.util.List;

public class SugerirInstructorUseCase implements SugerirInstructorInputPort {

    private final ValidarElegibilidadInstructor validarElegibilidadInstructor;
    private final DisponibilidadInstructorRepository disponibilidadInstructorRepository;
    private final InstructorEspecialidadRepository instructorEspecialidadRepository;
    private final CompetenciaEspecialidadRepository competenciaEspecialidadRepository;
    private final ProgramaRepository programaRepository;

    public SugerirInstructorUseCase(
            ValidarElegibilidadInstructor validarElegibilidadInstructor,
            DisponibilidadInstructorRepository disponibilidadInstructorRepository,
            InstructorEspecialidadRepository instructorEspecialidadRepository,
            CompetenciaEspecialidadRepository competenciaEspecialidadRepository,
            ProgramaRepository programaRepository) {

        this.validarElegibilidadInstructor = validarElegibilidadInstructor;
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
        this.instructorEspecialidadRepository = instructorEspecialidadRepository;
        this.competenciaEspecialidadRepository = competenciaEspecialidadRepository;
        this.programaRepository = programaRepository;
    }

    @Override
    public List<DisponibilidadInstructor> sugerirInstructores(Long competenciaId, Long programaId, Long horasRequeridas) {
        var competenciaEspecialidad = competenciaEspecialidadRepository.findByCompetenciaId(competenciaId)
                .orElse(null);

        if (competenciaEspecialidad == null) {
            return List.of();
        }

        var programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new RuntimeException("No se encontró el programa con el id " + programaId));

        Comparator<DisponibilidadInstructor> comparadorSugerencia = Comparator
                .comparingInt((DisponibilidadInstructor d) -> d.getDiasDisponibles() != null ? d.getDiasDisponibles().size() : 0)
                .thenComparingLong(DisponibilidadInstructor::getHorasDisponibles)
                .reversed();

        return disponibilidadInstructorRepository.findAll().stream()
                .filter(disponibilidad -> {
                    var instructorEspecialidad = instructorEspecialidadRepository
                            .findByInstructorId(disponibilidad.getUsuarioId())
                            .orElse(null);

                    if (instructorEspecialidad == null) {
                        return false;
                    }

                    return validarElegibilidadInstructor.esElegible(
                            disponibilidad,
                            instructorEspecialidad,
                            competenciaEspecialidad,
                            programa,
                            horasRequeridas
                    );
                })
                .sorted(comparadorSugerencia)
                .toList();
    }
}