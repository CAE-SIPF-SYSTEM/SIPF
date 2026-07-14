package com.caeproject.cae.domain.ports.in.CompetenciaEspecialidad;

import com.caeproject.cae.application.usecases.CompetenciaEspecialidad.commands.EditarEspecialidadCompetenciaCommand;
import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;

public interface EditarEspecialidadCompetenciaInputPort {
    CompetenciaEspecialidad editarEspecialidadCompetencia(EditarEspecialidadCompetenciaCommand command, Long competenciaId);
}
