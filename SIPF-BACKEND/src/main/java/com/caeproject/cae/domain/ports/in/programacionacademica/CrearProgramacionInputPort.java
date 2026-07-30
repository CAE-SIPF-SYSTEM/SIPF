package com.caeproject.cae.domain.ports.in.programacionacademica;

import com.caeproject.cae.application.usecases.programacionacademica.commands.CrearProgramacionCommand;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;

public interface CrearProgramacionInputPort {
    ProgramacionAcademica programacionAcademica(CrearProgramacionCommand crearProgramacionCommand);
}
