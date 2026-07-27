package com.caeproject.cae.application.usecases.asignacioninstructor;

import com.caeproject.cae.application.usecases.asignacioninstructor.commands.AsignarInstructorCommand;
import com.caeproject.cae.domain.ports.exceptions.asignacionexceptions.HorasInsuficientesException;
import com.caeproject.cae.domain.ports.exceptions.asignacionexceptions.InstructorEspecialidadIncompatibleException;
import com.caeproject.cae.domain.ports.exceptions.disponibilidadinstructorexception.DisponibilidadNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.in.asignarinstructor.AsignarInstructorInputPort;
import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.out.*;

public class AsignacionInstructorUseCase implements AsignarInstructorInputPort {

        private final InstructorEspecialidadRepository instructorEspecialidadRepository;
        private final CompetenciaEspecialidadRepository competenciaEspecialidadRepository;
        private final DisponibilidadInstructorRepository disponibilidadInstructorRepository;
        private final ProgramaRepository programaRepository;
        private final DiseñoCurricularRepository diseñoCurricularRepository;

        public AsignacionInstructorUseCase(InstructorEspecialidadRepository instructorEspecialidadRepository, CompetenciaEspecialidadRepository competenciaEspecialidadRepository, DisponibilidadInstructorRepository disponibilidadInstructorRepository, ProgramaRepository programaRepository, DiseñoCurricularRepository diseñoCurricularRepository) {
                this.instructorEspecialidadRepository = instructorEspecialidadRepository;
                this.competenciaEspecialidadRepository = competenciaEspecialidadRepository;
                this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
                this.programaRepository = programaRepository;
                this.diseñoCurricularRepository = diseñoCurricularRepository;
        }



        private void validarInstructorEspecialidad(AsignarInstructorCommand command) {
                CompetenciaEspecialidad competenciaEspecialidad = competenciaEspecialidadRepository.findByCompetenciaId(command.competenciaId())
                        .orElseThrow(() -> new RuntimeException("No se encontró la especialidad con este id " + command.competenciaId()));

                InstructorEspecialidad instructorEspecialidad = instructorEspecialidadRepository.findByInstructorId(command.usuarioId())
                        .orElseThrow(() -> new RuntimeException("No se encontró la especialidad del instructor con este id " + command.usuarioId()));

                if (!instructorEspecialidad.getEspecialidadId().equals(competenciaEspecialidad.getEspecialidadId())) {
                        throw new InstructorEspecialidadIncompatibleException("La especialidad del instructor no es compatible con la especialidad de la competencia");
                }
        }

        public Long validarInstructorDisponibilidadHoras(AsignarInstructorCommand command, DisponibilidadInstructor disponibilidadInstructor){
                Long horasDisponibles = disponibilidadInstructor.getHorasDisponibles();
                Long horasTotales = Long.valueOf(diseñoCurricularRepository.sumarHorasPorCompetenciaYPrograma(command.competenciaId(), command.programaId()));

                if (horasDisponibles < horasTotales){
                        throw new HorasInsuficientesException("El instructor no tiene suficientes horas disponibles para asignar a la competencia");
                }
                Long horasAsignadas = horasTotales;
                return horasAsignadas;
        }

        private void validarMunicipio(Programa programa, DisponibilidadInstructor disponibilidadInstructor) {
                boolean municipioCompatible = disponibilidadInstructor.getMunicipios().stream()
                        .anyMatch(m -> m.getId().equals(programa.getMunicipio().getId()));

                if (!municipioCompatible) {
                        throw new RuntimeException("El municipio del instructor no es compatible con el municipio del programa");
                }
        }



        @Override
        public Long asignarInstructor(AsignarInstructorCommand command) {

                DisponibilidadInstructor disponibilidadInstructor = disponibilidadInstructorRepository.findById(command.usuarioId())
                        .orElseThrow(() -> new DisponibilidadNoEncontradaException(command.usuarioId()));

                Programa programa = programaRepository.findById(command.programaId())
                        .orElseThrow(() -> new ProgramaNoEncontradoException(command.programaId()));

                validarInstructorEspecialidad(command);
                Long horasRequeridas = validarInstructorDisponibilidadHoras(command, disponibilidadInstructor);
                validarMunicipio(programa, disponibilidadInstructor);

                disponibilidadInstructor.comprometerHoras(horasRequeridas);

                disponibilidadInstructorRepository.saveDisponibilidad(disponibilidadInstructor);

                return disponibilidadInstructor.getUsuarioId();
        }
}
