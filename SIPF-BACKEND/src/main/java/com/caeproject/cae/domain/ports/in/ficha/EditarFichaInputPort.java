package com.caeproject.cae.domain.ports.in.ficha;

import com.caeproject.cae.application.usecases.ficha.commands.EditarFichaCommand;
import com.caeproject.cae.domain.ports.model.ficha.Ficha;

public interface EditarFichaInputPort {
    Ficha editarFicha(EditarFichaCommand command, Long id);
}
