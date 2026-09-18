package com.caeproject.cae.domain.ports.in.competencia;

import com.caeproject.cae.application.usecases.competencia.commands.CrearCompetenciaCommand;
import com.caeproject.cae.domain.ports.model.Competencia;

public interface CrearCompetenciaInputPort {
    Competencia crearCompetencia(CrearCompetenciaCommand command);
}
