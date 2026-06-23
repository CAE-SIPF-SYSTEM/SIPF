package com.caeproject.cae.domain.ports.in.programa;

import com.caeproject.cae.application.usecases.programa.commands.EditarProgramaCommand;
import com.caeproject.cae.domain.ports.model.programa.Programa;

public interface EditarProgramaInputPort {
    Programa editarPrograma(EditarProgramaCommand command, Long id);
}
