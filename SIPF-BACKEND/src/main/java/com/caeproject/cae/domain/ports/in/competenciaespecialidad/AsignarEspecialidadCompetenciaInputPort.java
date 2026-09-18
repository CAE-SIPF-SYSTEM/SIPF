package com.caeproject.cae.domain.ports.in.competenciaespecialidad;

import com.caeproject.cae.application.usecases.competenciaespecialidad.commands.AsignarEspecialidadCompetenciaCommand;
import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;

public interface AsignarEspecialidadCompetenciaInputPort {
    CompetenciaEspecialidad asignarEspecialidadCompetencia(AsignarEspecialidadCompetenciaCommand command);
}
