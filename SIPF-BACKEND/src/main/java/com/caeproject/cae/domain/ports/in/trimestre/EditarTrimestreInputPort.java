package com.caeproject.cae.domain.ports.in.trimestre;

import com.caeproject.cae.application.usecases.trimestre.commands.EditarTrimestreCommand;
import com.caeproject.cae.domain.ports.model.Trimestre;

public interface EditarTrimestreInputPort {
    Trimestre editarTrimestre (EditarTrimestreCommand command, Long id);
}
