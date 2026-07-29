package com.caeproject.cae.application.usecases.rap;

import com.caeproject.cae.application.usecases.rap.commands.CrearRapCommand;
import com.caeproject.cae.domain.ports.in.Rap.CrearRapInputPort;
import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.domain.ports.out.RapRepository;

public class CrearRapUseCase implements CrearRapInputPort {

    private final RapRepository rapRepository;

    public CrearRapUseCase(RapRepository rapRepository) {
        this.rapRepository = rapRepository;
    }

    @Override
    public Rap createRap(CrearRapCommand command) {
        Rap rap = new Rap();
        rap.setCompetenciaId(command.getCompetenciaId());
        rap.setDescripcion(command.getDescripcion());

        return rapRepository.saveRap(rap);
    }
}
