package com.caeproject.cae.application.usecases.programacionacademica;

import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaNoEncontradaException;
import com.caeproject.cae.domain.ports.in.asignarinstructor.SugerirInstructorInputPort;
import com.caeproject.cae.domain.ports.in.programacionacademica.ObtenerResumenFichaInputPort;
import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.infraestructure.dtos.asignacioninstructor.InstructorSugeridoResponse;
import com.caeproject.cae.infraestructure.dtos.asignacioninstructor.ResumenCompetenciaResponse;
import com.caeproject.cae.infraestructure.dtos.asignacioninstructor.ResumenFichaCompetenciasResponse;

import java.util.ArrayList;
import java.util.List;

public class ObtenerResumenFichaUseCase implements ObtenerResumenFichaInputPort {

    private final FichaRepository fichaRepository;
    private final CompetenciaRepository competenciaRepository;
    private final DiseñoCurricularRepository disenoCurricularRepository;
    private final SugerirInstructorInputPort sugerirInstructorInputPort;

    public ObtenerResumenFichaUseCase(SugerirInstructorInputPort sugerirInstructorInputPort, FichaRepository fichaRepository, CompetenciaRepository competenciaRepository, DiseñoCurricularRepository disenoCurricularRepository) {
        this.fichaRepository = fichaRepository;
        this.competenciaRepository = competenciaRepository;
        this.disenoCurricularRepository = disenoCurricularRepository;
        this.sugerirInstructorInputPort = sugerirInstructorInputPort;
    }

    @Override
    public ResumenFichaCompetenciasResponse obtenerResumenFicha(Long fichaId) {

        Ficha ficha = fichaRepository.findById(fichaId)
                .orElseThrow(() -> new FichaNoEncontradaException(fichaId));

        Long programaId = ficha.getProgramaId();

        List<Competencia> todasLasCompetencias = competenciaRepository.findAll();
        List<ResumenCompetenciaResponse> listaResumenesCompetencias = new ArrayList<>();

        for (Competencia competencia : todasLasCompetencias) {
            Integer horasRaw = disenoCurricularRepository.sumarHorasPorCompetenciaYPrograma(competencia.getId(), programaId);
            Long totalHoras = (horasRaw != null) ? horasRaw.longValue() : 0L;

            if (totalHoras > 0) {
                List<DisponibilidadInstructor> instructoresSugeridos = sugerirInstructorInputPort
                        .sugerirInstructores(competencia.getId(), fichaId, totalHoras);

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

        ResumenFichaCompetenciasResponse response = new ResumenFichaCompetenciasResponse();
        response.setFichaId(fichaId);
        response.setCodigoFicha(ficha.getCodigoFicha());
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
