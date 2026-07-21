package com.caeproject.cae.application.usecases.ProgramacionAcademica;

import com.caeproject.cae.domain.ports.in.ProgramacionAcademica.ListarProgramacionInputPort;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;
import com.caeproject.cae.domain.ports.out.ProgramacionAcademicaRepository;

import java.util.List;

public class ListarProgramacionAcademicaUseCase implements ListarProgramacionInputPort {

    private final ProgramacionAcademicaRepository programacionAcademicaRepository;

    public ListarProgramacionAcademicaUseCase(ProgramacionAcademicaRepository programacionAcademicaRepository) {
        this.programacionAcademicaRepository = programacionAcademicaRepository;
    }

    @Override
    public List<ProgramacionAcademica> listarProgramaciones() {
        return programacionAcademicaRepository.findAll();
    }
}
