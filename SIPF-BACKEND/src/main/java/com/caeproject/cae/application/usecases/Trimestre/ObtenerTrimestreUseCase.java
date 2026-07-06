package com.caeproject.cae.application.usecases.Trimestre;

import com.caeproject.cae.domain.ports.in.trimestre.ObtenerTrimestreInputPort;
import com.caeproject.cae.domain.ports.model.Trimestre;
import com.caeproject.cae.domain.ports.out.TrimestreRepository;

import java.util.List;

public class ObtenerTrimestreUseCase implements ObtenerTrimestreInputPort {

    private final TrimestreRepository trimestreRepository;

    public ObtenerTrimestreUseCase (TrimestreRepository trimestreRepository){
        this.trimestreRepository = trimestreRepository;
    }

     @Override
    public Trimestre obtenerTrimestre(Long id) {
        return trimestreRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Trimestre no encontrado"));
    }

    @Override
    public List<Trimestre> obtenerTrimestreFicha(Long fichaId) {
       List<Trimestre> trimestres = trimestreRepository.findByFicha(fichaId);
       return  trimestres;
    }
}
