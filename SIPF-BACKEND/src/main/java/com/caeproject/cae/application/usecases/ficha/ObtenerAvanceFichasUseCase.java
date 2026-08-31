package com.caeproject.cae.application.usecases.ficha;

import com.caeproject.cae.domain.ports.in.ficha.ObtenerAvanceFichasInputPort;
import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import com.caeproject.cae.domain.ports.model.Ficha;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.out.ProgramacionAcademicaRepository;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.domain.ports.out.RapRepository;
import com.caeproject.cae.infraestructure.dtos.ficha.FichaAvanceResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ObtenerAvanceFichasUseCase implements ObtenerAvanceFichasInputPort {
    private final FichaRepository fichaRepository;
    private final ProgramaRepository programaRepository;
    private final ProgramacionAcademicaRepository programacionAcademicaRepository;
    private final DiseñoCurricularRepository diseñoCurricularRepository;
    private final RapRepository rapRepository;

    public ObtenerAvanceFichasUseCase(
            FichaRepository fichaRepository,
            ProgramaRepository programaRepository,
            ProgramacionAcademicaRepository programacionAcademicaRepository,
            DiseñoCurricularRepository diseñoCurricularRepository,
            RapRepository rapRepository) {
        this.fichaRepository = fichaRepository;
        this.programaRepository = programaRepository;
        this.programacionAcademicaRepository = programacionAcademicaRepository;
        this.diseñoCurricularRepository = diseñoCurricularRepository;
        this.rapRepository = rapRepository;
    }

    @Override
    public List<FichaAvanceResponse> obtenerAvanceFichas() {
        List<Ficha> fichas = fichaRepository.findAll();
        if (fichas == null || fichas.isEmpty()) {
            return new ArrayList<>();
        }

        List<Programa> programas = programaRepository.findAll();
        Map<Long, String> programaMap = programas.stream()
                .collect(Collectors.toMap(Programa::getId, Programa::getNombre, (a, b) -> a));

        List<ProgramacionAcademica> todasProgramaciones = programacionAcademicaRepository.findAll();
        List<DiseñoCurricular> todosMallas = diseñoCurricularRepository.findAll();
        List<Rap> todosRapsBD = rapRepository.findAll();

        List<FichaAvanceResponse> resultado = new ArrayList<>();

        for (Ficha f : fichas) {
            String progNombre = programaMap.getOrDefault(f.getProgramaId(), "Programa Técnico");

            // RAPs asignados a esta ficha específica
            Set<Long> rapsAsignadosFicha = todasProgramaciones.stream()
                    .filter(p -> p.getFichaId() != null && p.getFichaId().equals(f.getId()))
                    .map(ProgramacionAcademica::getRapId)
                    .filter(rapId -> rapId != null)
                    .collect(Collectors.toSet());

            int rapsVistos = rapsAsignadosFicha.size();

            // Total de RAPs según la malla curricular del programa
            long rapsMallaCount = todosMallas.stream()
                    .filter(m -> m.getProgramaId() != null && m.getProgramaId().equals(f.getProgramaId()))
                    .map(DiseñoCurricular::getRapId)
                    .distinct()
                    .count();

            int totalRaps = (int) rapsMallaCount;
            if (totalRaps == 0) {
                // Si la malla curricular no está cargada, usar el total de RAPs del sistema o el número de asignados
                totalRaps = Math.max(todosRapsBD.size(), rapsVistos);
                if (totalRaps == 0) totalRaps = 11; // Fallback por defecto si BD no tiene RAPs
            }

            int rapsPendientes = Math.max(0, totalRaps - rapsVistos);
            int porcentaje = totalRaps > 0 ? (int) Math.round(((double) rapsVistos / totalRaps) * 100) : 0;
            if (porcentaje > 100) porcentaje = 100;

            String estado = "En Progreso";
            if (porcentaje == 100) {
                estado = "Completado";
            } else if (porcentaje == 0) {
                estado = "Sin Asignación";
            } else if (porcentaje < 40) {
                estado = "Crítico";
            }

            resultado.add(FichaAvanceResponse.builder()
                    .fichaId(f.getId())
                    .codigoFicha(f.getCodigoFicha())
                    .programaId(f.getProgramaId())
                    .programaNombre(progNombre)
                    .jornada("MAÑANA") // Se puede adaptar dinámicamente según BD
                    .rapsVistos(rapsVistos)
                    .rapsPendientes(rapsPendientes)
                    .totalRaps(totalRaps)
                    .porcentajeAvance(porcentaje)
                    .estado(estado)
                    .build());
        }

        return resultado;
    }
}
