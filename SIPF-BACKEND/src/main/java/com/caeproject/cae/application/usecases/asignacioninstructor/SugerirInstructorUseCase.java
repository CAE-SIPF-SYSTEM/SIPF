package com.caeproject.cae.application.usecases.asignacioninstructor;

import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.in.asignarinstructor.SugerirInstructorInputPort;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.out.*;
import com.caeproject.cae.domain.ports.service.ValidarElegibilidadInstructor;

import java.util.Comparator;
import java.util.List;

public class SugerirInstructorUseCase implements SugerirInstructorInputPort {

    private final ValidarElegibilidadInstructor validarElegibilidadInstructor;
    private final DisponibilidadInstructorRepository disponibilidadInstructorRepository;
    private final InstructorEspecialidadRepository instructorEspecialidadRepository;
    private final CompetenciaEspecialidadRepository competenciaEspecialidadRepository;
    private final ProgramaRepository programaRepository;
    private final FichaRepository fichaRepository;

    public SugerirInstructorUseCase(ValidarElegibilidadInstructor validarElegibilidadInstructor, DisponibilidadInstructorRepository disponibilidadInstructorRepository, InstructorEspecialidadRepository instructorEspecialidadRepository, CompetenciaEspecialidadRepository competenciaEspecialidadRepository, ProgramaRepository programaRepository, FichaRepository fichaRepository) {
        this.validarElegibilidadInstructor = validarElegibilidadInstructor;
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
        this.instructorEspecialidadRepository = instructorEspecialidadRepository;
        this.competenciaEspecialidadRepository = competenciaEspecialidadRepository;
        this.programaRepository = programaRepository;
        this.fichaRepository = fichaRepository;
    }

    @Override
    public List<DisponibilidadInstructor> sugerirInstructores(Long competenciaId, Long fichaId, Long horasRequeridas) {
        var competenciaEspecialidad = competenciaEspecialidadRepository.findByCompetenciaId(competenciaId)
                .orElse(null);

        var ficha = fichaRepository.findById(fichaId)
                .orElseThrow(() -> new FichaNoEncontradaException(fichaId));

        Programa programa = programaRepository.findById(ficha.getProgramaId())
                .orElseThrow(() -> new ProgramaNoEncontradoException(ficha.getProgramaId()));

        Comparator<DisponibilidadInstructor> comparadorSugerencia = Comparator
                .comparingInt((DisponibilidadInstructor d) -> d.getDiasDisponibles() != null ? d.getDiasDisponibles().size() : 0)
                .thenComparingLong(DisponibilidadInstructor::getHorasDisponibles)
                .reversed();

        // Paso 1: Intentar buscar instructores con especialidad estricta requerida
        List<DisponibilidadInstructor> candidatos = disponibilidadInstructorRepository.findAll().stream()
                .filter(disponibilidad -> {
                    var instructorEspecialidad = instructorEspecialidadRepository
                            .findByInstructorId(disponibilidad.getUsuarioId())
                            .orElse(null);

                    return validarElegibilidadInstructor.esElegible(
                            disponibilidad,
                            instructorEspecialidad,
                            competenciaEspecialidad,
                            programa,
                            horasRequeridas,
                            true
                    );
                })
                .sorted(comparadorSugerencia)
                .toList();

        // Paso 2: Si los instructores especializados se quedaron sin horas (0h disponibles), hacer fallback a cualquier instructor activo con horas suficientes
        if (candidatos.isEmpty()) {
            candidatos = disponibilidadInstructorRepository.findAll().stream()
                    .filter(disponibilidad -> {
                        var instructorEspecialidad = instructorEspecialidadRepository
                                .findByInstructorId(disponibilidad.getUsuarioId())
                                .orElse(null);

                        return validarElegibilidadInstructor.esElegible(
                                disponibilidad,
                                instructorEspecialidad,
                                competenciaEspecialidad,
                                programa,
                                horasRequeridas,
                                false
                        );
                    })
                    .sorted(comparadorSugerencia)
                    .toList();
        }

        return candidatos;
    }
}