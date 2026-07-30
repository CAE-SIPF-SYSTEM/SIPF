package com.caeproject.cae.domain.ports.in.CompetenciaEspecialidad;

import com.caeproject.cae.application.usecases.CompetenciaEspecialidad.commands.AsignarEspecialidadCompetenciaCommand;
import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;

public interface AsignarEspecialidadCompetenciaInputPort {
    CompetenciaEspecialidad asignarEspecialidadCompetencia(AsignarEspecialidadCompetenciaCommand command);
}
