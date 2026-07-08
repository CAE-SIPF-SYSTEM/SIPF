package com.caeproject.cae.domain.ports.in.trimestre;

import com.caeproject.cae.application.usecases.trimestre.commands.CrearTrimestreCommand;
import com.caeproject.cae.domain.ports.model.Trimestre;

public interface CrearTrimestreInputPort {
    Trimestre crearTrimestre (CrearTrimestreCommand command);
}
