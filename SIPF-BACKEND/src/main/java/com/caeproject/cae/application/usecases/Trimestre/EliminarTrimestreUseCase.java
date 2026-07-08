package com.caeproject.cae.application.usecases.Trimestre;

import com.caeproject.cae.domain.ports.in.trimestre.EliminarTrimestreInputPort;
import com.caeproject.cae.domain.ports.out.TrimestreRepository;

public class EliminarTrimestreUseCase implements EliminarTrimestreInputPort {

    private final TrimestreRepository trimestreRepository;

    public EliminarTrimestreUseCase (TrimestreRepository trimestreRepository){
        this.trimestreRepository = trimestreRepository;
    }

    @Override
    public void eliminarTrimestre(Long id) {
        trimestreRepository.findById(id)
                .orElseThrow(() ->  new RuntimeException("FIcha no encontrada  " + id ));
        trimestreRepository.eliminarTrimestre(id);
    }
}
