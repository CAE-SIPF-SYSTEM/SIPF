package com.caeproject.cae.application.usecases.programa;

import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.in.programa.ObtenerProgramaInputPort;
import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;

import java.util.List;

public class ObtenerProgramaUseCase implements ObtenerProgramaInputPort {
    private final ProgramaRepository programaRepository;

    public ObtenerProgramaUseCase (ProgramaRepository programaRepository){
        this.programaRepository = programaRepository;
    }

    @Override
    public Programa obtenerPrograma(Long id) {
        return programaRepository.findById(id)
                .orElseThrow(()-> new ProgramaNoEncontradoException(id));

    }
    @Override
    public List<Programa> obtenerProgramaJornada(Jornada jornada){
        return programaRepository.findByJornada(jornada);
    }

    public List<Programa> obtenerProgramaNivelFormacion(NivelFormacion nivelFormacion){
        return programaRepository.findByNiveldeFormacion(nivelFormacion);
    }
}
