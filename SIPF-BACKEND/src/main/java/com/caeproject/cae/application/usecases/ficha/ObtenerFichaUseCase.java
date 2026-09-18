package com.caeproject.cae.application.usecases.ficha;

import com.caeproject.cae.domain.ports.in.ficha.ObtenerFIchaInputPort;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.model.Ficha;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaNoEncontradaException;

import java.util.List;

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

    @Override
    public List<Ficha> obtenerFichaPorProgramaId(Long programaId) {
        List<Ficha> fichas = fichaRepository.findByProgramaId(programaId);

        if (fichas == null) {
            throw new FichaNoEncontradaException(programaId);
        }
        return fichas;
    }
}
