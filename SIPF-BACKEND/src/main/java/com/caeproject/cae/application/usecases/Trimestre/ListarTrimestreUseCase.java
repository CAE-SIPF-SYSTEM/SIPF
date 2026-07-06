package com.caeproject.cae.application.usecases.Trimestre;

import com.caeproject.cae.domain.ports.in.trimestre.ListarTrimestreInputPort;
import com.caeproject.cae.domain.ports.model.Trimestre;
import com.caeproject.cae.domain.ports.out.TrimestreRepository;

import java.util.List;

public class ListarTrimestreUseCase implements ListarTrimestreInputPort {

    private final TrimestreRepository trimestreRepository;

    public ListarTrimestreUseCase (TrimestreRepository trimestreRepository) {
        this.trimestreRepository = trimestreRepository;
    }

    @Override
    public List<Trimestre> listarTrimestres() {
        return trimestreRepository.findAll();
    }
}
