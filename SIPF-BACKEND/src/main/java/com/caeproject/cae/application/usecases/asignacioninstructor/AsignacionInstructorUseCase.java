package com.caeproject.cae.application.usecases.asignacioninstructor;

import com.caeproject.cae.application.usecases.asignacioninstructor.commands.AsignarInstructorCommand;
import com.caeproject.cae.domain.ports.exceptions.asignacionexceptions.InstructorEspecialidadIncompatibleException;
import com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception.DisponibilidadNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.in.asignarinstructor.AsignarInstructorInputPort;
import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.out.*;
import com.caeproject.cae.domain.ports.service.ValidarElegibilidadInstructor;

public class AsignacionInstructorUseCase implements AsignarInstructorInputPort {

    private final InstructorEspecialidadRepository instructorEspecialidadRepository;
    private final CompetenciaEspecialidadRepository competenciaEspecialidadRepository;
    private final DisponibilidadInstructorRepository disponibilidadInstructorRepository;
    private final ProgramaRepository programaRepository;
    private final DiseñoCurricularRepository diseñoCurricularRepository;
    private final ValidarElegibilidadInstructor validarElegibilidadInstructor;

    public AsignacionInstructorUseCase(
            InstructorEspecialidadRepository instructorEspecialidadRepository,
            CompetenciaEspecialidadRepository competenciaEspecialidadRepository,
            DisponibilidadInstructorRepository disponibilidadInstructorRepository,
            ProgramaRepository programaRepository,
            DiseñoCurricularRepository diseñoCurricularRepository,
            ValidarElegibilidadInstructor validarElegibilidadInstructor) {

        this.instructorEspecialidadRepository = instructorEspecialidadRepository;
        this.competenciaEspecialidadRepository = competenciaEspecialidadRepository;
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
        this.programaRepository = programaRepository;
        this.diseñoCurricularRepository = diseñoCurricularRepository;
        this.validarElegibilidadInstructor = validarElegibilidadInstructor;
    }

    @Override
    public Long asignarInstructor(AsignarInstructorCommand command) {

        DisponibilidadInstructor disponibilidadInstructor = disponibilidadInstructorRepository.findById(command.usuarioId())
                .orElseThrow(() -> new DisponibilidadNoEncontradaException(command.usuarioId()));

        Programa programa = programaRepository.findById(command.programaId())
                .orElseThrow(() -> new ProgramaNoEncontradoException(command.programaId()));

        CompetenciaEspecialidad competenciaEspecialidad = competenciaEspecialidadRepository.findByCompetenciaId(command.competenciaId())
                .orElseThrow(() -> new RuntimeException("No se encontró la especialidad con este id " + command.competenciaId()));

        InstructorEspecialidad instructorEspecialidad = instructorEspecialidadRepository.findByInstructorId(command.usuarioId())
                .orElseThrow(() -> new RuntimeException("No se encontró la especialidad del instructor con este id " + command.usuarioId()));

        Long horasTotales = Long.valueOf(diseñoCurricularRepository.sumarHorasPorCompetenciaYPrograma(command.competenciaId(), command.programaId()));


        boolean esElegible = validarElegibilidadInstructor.esElegible(
                disponibilidadInstructor,
                instructorEspecialidad,
                competenciaEspecialidad,
                programa,
                horasTotales
        );

        if (!esElegible) {
            throw new InstructorEspecialidadIncompatibleException(
                    "El instructor no cumple con todos los criterios de elegibilidad (especialidad, municipio, días u horas)."
            );
        }

        //agarra horas del instructor
        disponibilidadInstructor.comprometerHoras(horasTotales);

        disponibilidadInstructorRepository.saveDisponibilidad(disponibilidadInstructor);

        return disponibilidadInstructor.getUsuarioId();
    }
}
