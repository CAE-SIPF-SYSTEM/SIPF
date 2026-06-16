package com.caeproject.cae.application.usecases.ficha;

import com.caeproject.cae.domain.ports.in.ficha.ObtenerFIchaInputPort;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.model.ficha.Ficha;
import com.caeproject.cae.domain.ports.exceptions.fichas_ProgramasException.FichaNoEncontradaException;

public class ObtenerFichaUseCase implements ObtenerFIchaInputPort {
    private final FichaRepository fichaRepository;

    public ObtenerFichaUseCase(FichaRepository fichaRepository) {
        this.fichaRepository = fichaRepository;
    }

    @Override
    public Ficha obtenerFicha(Long id) {
        return fichaRepository.findById(id)
            .orElseThrow(() -> new FichaNoEncontradaException(id));
    }
}
