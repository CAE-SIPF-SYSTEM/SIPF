package com.caeproject.cae.domain.ports.in.competenciaespecialidad;

import com.caeproject.cae.application.usecases.competenciaespecialidad.commands.EditarEspecialidadCompetenciaCommand;
import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;

public interface EditarEspecialidadCompetenciaInputPort {
    CompetenciaEspecialidad editarEspecialidadCompetencia(EditarEspecialidadCompetenciaCommand command, Long competenciaId);
}
