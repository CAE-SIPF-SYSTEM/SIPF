package com.caeproject.cae.application.usecases.rap;

import com.caeproject.cae.application.usecases.rap.commands.EditarRapCommand;
import com.caeproject.cae.domain.ports.exceptions.rapexception.RapNoEncontradoException;
import com.caeproject.cae.domain.ports.in.rap.EditarRapInputPort;
import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.domain.ports.out.RapRepository;

public class EditarRapUseCase implements EditarRapInputPort {

    private final RapRepository rapRepository;

    public EditarRapUseCase(RapRepository rapRepository) {
        this.rapRepository = rapRepository;
    }

    @Override
    public Rap editarRap(EditarRapCommand command, Long id) {
        Rap rapExistente = rapRepository.findById(id)
                .orElseThrow(() -> new RapNoEncontradoException(id));

        if (command.getDescripcion() != null) {
            rapExistente.setDescripcion(command.getDescripcion());
        }
        if (command.getCompetenciaId() != null) {
            rapExistente.setCompetenciaId(command.getCompetenciaId());
        }

        return rapRepository.saveRap(rapExistente);
    }
}
