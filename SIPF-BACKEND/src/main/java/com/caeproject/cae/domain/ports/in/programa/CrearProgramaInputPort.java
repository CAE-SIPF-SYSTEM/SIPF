package com.caeproject.cae.domain.ports.in.programa;

import com.caeproject.cae.application.usecases.programa.commands.CrearProgramaCommand;
import com.caeproject.cae.domain.ports.model.programa.Programa;

public interface CrearProgramaInputPort {
    Programa crearPrograma(CrearProgramaCommand command);
}
