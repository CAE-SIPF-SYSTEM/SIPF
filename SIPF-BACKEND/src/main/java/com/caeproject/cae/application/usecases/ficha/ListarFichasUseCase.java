package com.caeproject.cae.application.usecases.ficha;

import com.caeproject.cae.domain.ports.in.ficha.ListarFichasInputPort;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.domain.ports.model.ficha.Ficha;

import java.util.List;

public class ListarFichasUseCase implements ListarFichasInputPort {
    private final FichaRepository fichaRepository;

    public ListarFichasUseCase(FichaRepository fichaRepository) {
        this.fichaRepository = fichaRepository;
    }

    @Override
    public List<Ficha> listarFichas() {
        return fichaRepository.findAll();
    }
}
