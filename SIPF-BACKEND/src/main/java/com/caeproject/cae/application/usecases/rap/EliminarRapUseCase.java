package com.caeproject.cae.application.usecases.rap;

import com.caeproject.cae.domain.ports.exceptions.rapexception.RapNoEncontradoException;
import com.caeproject.cae.domain.ports.in.rap.EliminarRapInputPort;
import com.caeproject.cae.domain.ports.out.RapRepository;

public class EliminarRapUseCase implements EliminarRapInputPort {

    private final RapRepository rapRepository;

    public EliminarRapUseCase(RapRepository rapRepository) {
        this.rapRepository = rapRepository;
    }

    @Override
    public void eliminarRap(Long id) {
        rapRepository.findById(id)
                .orElseThrow(() -> new RapNoEncontradoException(id));
        rapRepository.eliminarRap(id);
    }
}
