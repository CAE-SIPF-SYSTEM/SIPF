package com.caeproject.cae.application.usecases.ProgramacionAcademica;

import com.caeproject.cae.domain.ports.in.ProgramacionAcademica.EliminarProgramacionInputPort;
import com.caeproject.cae.domain.ports.out.ProgramacionAcademicaRepository;

public class EliminarProgramacionAcademicaUseCase implements EliminarProgramacionInputPort {

    private final ProgramacionAcademicaRepository programacionAcademicaRepository;

    public EliminarProgramacionAcademicaUseCase(ProgramacionAcademicaRepository programacionAcademicaRepository) {
        this.programacionAcademicaRepository = programacionAcademicaRepository;
    }

    @Override
    public void eliminarProgramacion(Long id) {
        programacionAcademicaRepository.eliminarProgramacionAcademica(id);
    }
}
