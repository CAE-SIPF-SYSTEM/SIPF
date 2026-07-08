package com.caeproject.cae.application.usecases.rap;

import com.caeproject.cae.domain.ports.exceptions.rapexception.RapNoEncontradoException;
import com.caeproject.cae.domain.ports.in.Rap.ObtenerRapInputPort;
import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.domain.ports.out.RapRepository;

import java.util.List;

public class ObtenerRapUseCase implements ObtenerRapInputPort {

    private final RapRepository rapRepository;

    public ObtenerRapUseCase(RapRepository rapRepository) {
        this.rapRepository = rapRepository;
    }

    @Override
    public Rap obtenerRap(Long id) {
        return rapRepository.findById(id)
                .orElseThrow(() -> new RapNoEncontradoException(id));
    }

    @Override
    public List<Rap> obtenerRapCompetencia(Long competenciaId) {
        return rapRepository.findByCompetencia(competenciaId);
    }
}
