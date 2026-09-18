package com.caeproject.cae.domain.ports.in.rap;

import com.caeproject.cae.application.usecases.rap.commands.CrearRapCommand;
import com.caeproject.cae.domain.ports.model.Rap;

public interface CrearRapInputPort {
    Rap createRap(CrearRapCommand command);
}
