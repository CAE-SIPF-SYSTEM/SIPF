package com.caeproject.cae.domain.ports.in.Rap;

import com.caeproject.cae.application.usecases.rap.commands.EditarRapCommand;
import com.caeproject.cae.domain.ports.model.rap.Rap;

public interface EditarRapInputPort {
    Rap editarRap(EditarRapCommand command, Long id);
}
