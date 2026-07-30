package com.caeproject.cae.domain.ports.in.rap;

import com.caeproject.cae.application.usecases.rap.commands.EditarRapCommand;
import com.caeproject.cae.domain.ports.model.Rap;

public interface EditarRapInputPort {
    Rap editarRap(EditarRapCommand command, Long id);
}
