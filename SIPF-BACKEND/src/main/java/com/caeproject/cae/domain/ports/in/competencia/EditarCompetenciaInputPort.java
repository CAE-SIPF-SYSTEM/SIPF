package com.caeproject.cae.domain.ports.in.competencia;

import com.caeproject.cae.application.usecases.competencia.commands.EditarCompetenciaCommand;
import com.caeproject.cae.domain.ports.model.competencia.Competencia;

public interface EditarCompetenciaInputPort {
    Competencia editarCompetencia(EditarCompetenciaCommand command, Long id);
}
