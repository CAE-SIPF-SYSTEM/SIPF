package com.caeproject.cae.application.usecases.asignacioninstructor;

import com.caeproject.cae.domain.ports.in.asignarinstructor.ObtenerResumenProgramaInputPort;
import com.caeproject.cae.domain.ports.in.asignarinstructor.SugerirInstructorInputPort;
import com.caeproject.cae.domain.ports.model.Competencia;
import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.infraestructure.dtos.asignacioninstructor.InstructorSugeridoResponse;
import com.caeproject.cae.infraestructure.dtos.asignacioninstructor.ResumenCompetenciaResponse;
import com.caeproject.cae.infraestructure.dtos.asignacioninstructor.ResumenProgramaCompetenciasResponse;

import java.util.ArrayList;
import java.util.List;

public class ObtenerResumenProgramaUseCase implements ObtenerResumenProgramaInputPort {

    private final ProgramaRepository programaRepository;
    private final CompetenciaRepository competenciaRepository;
    private final DiseñoCurricularRepository diseñoCurricularRepository;
    private final SugerirInstructorInputPort sugerirInstructorInputPort;

    public ObtenerResumenProgramaUseCase(
            ProgramaRepository programaRepository,
            CompetenciaRepository competenciaRepository,
            DiseñoCurricularRepository diseñoCurricularRepository,
            SugerirInstructorInputPort sugerirInstructorInputPort) {
        this.programaRepository = programaRepository;
        this.competenciaRepository = competenciaRepository;
        this.diseñoCurricularRepository = diseñoCurricularRepository;
        this.sugerirInstructorInputPort = sugerirInstructorInputPort;
    }

    @Override
    public ResumenProgramaCompetenciasResponse obtenerResumenPrograma(Long programaId) {
        Programa programa = programaRepository.findById(programaId)
                .orElseThrow(() -> new RuntimeException("No se encontró el programa con id " + programaId));

        List<Competencia> todasLasCompetencias = competenciaRepository.findAll();
        List<ResumenCompetenciaResponse> listaResumenesCompetencias = new ArrayList<>();

        for (Competencia competencia : todasLasCompetencias) {
            Integer horasRaw = diseñoCurricularRepository.sumarHorasPorCompetenciaYPrograma(competencia.getId(), programaId);
            Long totalHoras = (horasRaw != null) ? horasRaw.longValue() : 0L;

            if (totalHoras > 0) {
                List<DisponibilidadInstructor> instructoresSugeridos = sugerirInstructorInputPort
                        .sugerirInstructores(competencia.getId(), programaId, totalHoras);

                List<InstructorSugeridoResponse> instructoresResponse = instructoresSugeridos.stream()
                        .map(this::toInstructorSugeridoResponse)
                        .toList();

                ResumenCompetenciaResponse resumenComp = new ResumenCompetenciaResponse();
                resumenComp.setCompetenciaId(competencia.getId());
                resumenComp.setNombreCompetencia(competencia.getNombre());
                resumenComp.setTotalHorasRequeridas(totalHoras);
                resumenComp.setInstructoresSugeridos(instructoresResponse);

                listaResumenesCompetencias.add(resumenComp);
            }
        }

        ResumenProgramaCompetenciasResponse response = new ResumenProgramaCompetenciasResponse();
        response.setProgramaId(programa.getId());
        response.setNombrePrograma(programa.getNombre());
        response.setCompetencias(listaResumenesCompetencias);

        return response;
    }

    private InstructorSugeridoResponse toInstructorSugeridoResponse(DisponibilidadInstructor disponibilidad) {
        InstructorSugeridoResponse response = new InstructorSugeridoResponse();
        response.setUsuarioId(disponibilidad.getUsuarioId());
        response.setDiasDisponibles(disponibilidad.getDiasDisponibles());
        response.setHorasMaximas(disponibilidad.getHorasMaximas());
        response.setHorasAsignadas(disponibilidad.getHorasAsignadas());
        response.setHorasDisponibles(disponibilidad.getHorasDisponibles());
        response.setJornada(disponibilidad.getJornada());
        return response;
    }
}
