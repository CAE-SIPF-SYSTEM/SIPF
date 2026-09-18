package com.caeproject.cae.application.usecases.competenciaespecialidad;

import com.caeproject.cae.application.usecases.competenciaespecialidad.commands.EditarEspecialidadCompetenciaCommand;
import com.caeproject.cae.domain.ports.exceptions.competenciaespecialidadexception.CompetenciaEspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.in.competenciaespecialidad.EditarEspecialidadCompetenciaInputPort;
import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;
import com.caeproject.cae.domain.ports.out.CompetenciaEspecialidadRepository;

public class EditarEspecialidadCompetenciaUseCase implements EditarEspecialidadCompetenciaInputPort {

    private final CompetenciaEspecialidadRepository competenciaEspecialidadRepository;

    public EditarEspecialidadCompetenciaUseCase(CompetenciaEspecialidadRepository competenciaEspecialidadRepository) {
        this.competenciaEspecialidadRepository = competenciaEspecialidadRepository;
    }

    @Override
    public CompetenciaEspecialidad editarEspecialidadCompetencia(EditarEspecialidadCompetenciaCommand command, Long competenciaId) {
        if (competenciaEspecialidadRepository.findByCompetenciaId(competenciaId).isEmpty()) {
            throw new CompetenciaEspecialidadNoEncontradaException("La competencia no tiene ninguna especialidad asignada");
        }
        
        CompetenciaEspecialidad actualizada = new CompetenciaEspecialidad(
                competenciaId,
                command.getEspecialidadId()
        );
        return competenciaEspecialidadRepository.saveCompetenciaEspecialidad(actualizada);
    }
}
