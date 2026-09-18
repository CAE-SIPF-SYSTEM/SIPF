package com.caeproject.cae.application.usecases.rap;

import com.caeproject.cae.domain.ports.in.rap.ListarRapsInputPort;
import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.domain.ports.out.RapRepository;

import java.util.List;

public class ListarRapsUseCase implements ListarRapsInputPort {

    private final RapRepository rapRepository;

    public ListarRapsUseCase(RapRepository rapRepository) {
        this.rapRepository = rapRepository;
    }

    @Override
    public List<Rap> listarRaps() {
        return rapRepository.findAll();
    }
}
