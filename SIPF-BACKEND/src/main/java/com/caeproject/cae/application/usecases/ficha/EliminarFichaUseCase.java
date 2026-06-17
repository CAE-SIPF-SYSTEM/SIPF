package com.caeproject.cae.application.usecases.ficha;

import com.caeproject.cae.domain.ports.in.ficha.EliminarFichaInputPort;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.FichaNoEncontradaException;

public class EliminarFichaUseCase implements EliminarFichaInputPort {
    private final FichaRepository fichaRepository;

    public EliminarFichaUseCase(FichaRepository fichaRepository) {
        this.fichaRepository = fichaRepository;
    }

    @Override
    public void eliminarFicha(Long id) {
        fichaRepository.findById(id)
            .orElseThrow(() -> new FichaNoEncontradaException(id));
        fichaRepository.deleteFicha(id);
    }
}
