package com.caeproject.cae.domain.ports.in.ProgramacionAcademica;

import com.caeproject.cae.application.usecases.ProgramacionAcademica.commands.CrearProgramacionCommand;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;

public interface CrearProgramacionInputPort {
    ProgramacionAcademica programacionAcademica(CrearProgramacionCommand crearProgramacionCommand);
}
