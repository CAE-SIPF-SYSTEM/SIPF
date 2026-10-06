package com.caeproject.cae.application.usecases.asignacioninstructor;

import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.asignarinstructor.SugerirInstructorInputPort;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.DisponibilidadTipoContrato;
import com.caeproject.cae.domain.ports.model.PerfilBase;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;
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
    private final PerfilBaseRepository perfilBaseRepository;

    public SugerirInstructorUseCase(PerfilBaseRepository perfilBaseRepository,    ValidarElegibilidadInstructor validarElegibilidadInstructor, DisponibilidadInstructorRepository disponibilidadInstructorRepository, InstructorEspecialidadRepository instructorEspecialidadRepository, CompetenciaEspecialidadRepository competenciaEspecialidadRepository, ProgramaRepository programaRepository, FichaRepository fichaRepository) {
        this.perfilBaseRepository = perfilBaseRepository;
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

        List<DisponibilidadTipoContrato> listaOrdenada = disponibilidadInstructorRepository.findAll().stream()
                .map(disponibilidad -> {
                    PerfilBase perfilBase = perfilBaseRepository.findById(disponibilidad.getUsuarioId()).orElse(null);
                    return new DisponibilidadTipoContrato(disponibilidad, perfilBase);
                })
                .sorted((d1, d2) -> {
                    TIpoContrato t1 = d1.perfilBase() != null ? d1.perfilBase().getTipoContrato() : null;
                    TIpoContrato t2 = d2.perfilBase() != null ? d2.perfilBase().getTipoContrato() : null;
                    
                    if (t1 == t2) return 0;
                    if (t1 == null) return 1;
                    if (t2 == null) return -1;
                    
                    return t2.name().compareTo(t1.name());
                })
                .toList();

        List<DisponibilidadInstructor> candidatos = listaOrdenada.stream()
                .map(DisponibilidadTipoContrato::disponibilidadInstructor)
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
                .toList();

        if (candidatos.isEmpty()) {
            candidatos = listaOrdenada.stream()
                    .map(DisponibilidadTipoContrato::disponibilidadInstructor)
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
                    .toList();
        }

        return candidatos;
    }
}