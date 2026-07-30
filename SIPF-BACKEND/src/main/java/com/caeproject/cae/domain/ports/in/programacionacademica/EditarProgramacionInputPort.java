package com.caeproject.cae.domain.ports.in.programacionacademica;

import com.caeproject.cae.application.usecases.programacionacademica.commands.EditarProgramacionCommand;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;

public interface EditarProgramacionInputPort {
    ProgramacionAcademica editarProgramacionAcademica(EditarProgramacionCommand editarProgramacionCommand, Long id);
}
