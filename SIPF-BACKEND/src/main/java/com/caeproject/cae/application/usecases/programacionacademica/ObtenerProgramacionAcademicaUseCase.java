package com.caeproject.cae.application.usecases.programacionacademica;

import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.in.programacionacademica.ObtenerProgramacionInputPort;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;
import com.caeproject.cae.domain.ports.out.ProgramacionAcademicaRepository;

import java.util.List;

public class ObtenerProgramacionAcademicaUseCase implements ObtenerProgramacionInputPort {

    private final ProgramacionAcademicaRepository programacionAcademicaRepository;

    public ObtenerProgramacionAcademicaUseCase(ProgramacionAcademicaRepository programacionAcademicaRepository) {
        this.programacionAcademicaRepository = programacionAcademicaRepository;
    }

    @Override
    public ProgramacionAcademica obtenerProgramacion(Long id) {
        return programacionAcademicaRepository.findById(id)
                .orElseThrow(()-> new ProgramaNoEncontradoException(id));
    }

    @Override
    public List<ProgramacionAcademica> findByUsuarioId(Long usuarioId) {
        return programacionAcademicaRepository.findByUserId(usuarioId);

    }

    @Override
    public List<ProgramacionAcademica> findByTrimestreId(Long trimestreId) {
        return programacionAcademicaRepository.findByTrimestre(trimestreId);
    }
}
