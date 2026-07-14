package com.caeproject.cae.application.usecases.CompetenciaEspecialidad;

import com.caeproject.cae.domain.ports.in.CompetenciaEspecialidad.ListarEspecialidadesCompetenciaInputPort;
import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;
import com.caeproject.cae.domain.ports.out.CompetenciaEspecialidadRepository;

import java.util.List;

public class ListarEspecialidadesCompetenciaUseCase implements ListarEspecialidadesCompetenciaInputPort {

    private final CompetenciaEspecialidadRepository competenciaEspecialidadRepository;

    public ListarEspecialidadesCompetenciaUseCase(CompetenciaEspecialidadRepository competenciaEspecialidadRepository) {
        this.competenciaEspecialidadRepository = competenciaEspecialidadRepository;
    }

    @Override
    public List<CompetenciaEspecialidad> listarEspecialidadesCompetencias() {
        return competenciaEspecialidadRepository.findAll();
    }
}
