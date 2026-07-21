package com.caeproject.cae.domain.ports.in.ProgramacionAcademica;

import com.caeproject.cae.application.usecases.ProgramacionAcademica.commands.EditarProgramacionCommand;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;

public interface EditarProgramacionInputPort {
    ProgramacionAcademica editarProgramacionAcademica(EditarProgramacionCommand editarProgramacionCommand, Long id);
}
