package com.caeproject.cae.application.usecases.programacionacademica;

import com.caeproject.cae.application.usecases.programacionacademica.commands.CrearProgramacionCommand;
import com.caeproject.cae.application.usecases.asignacioninstructor.commands.AsignarInstructorCommand;
import com.caeproject.cae.domain.ports.exceptions.rapexception.RapNoEncontradoException;
import com.caeproject.cae.domain.ports.in.programacionacademica.CrearProgramacionInputPort;
import com.caeproject.cae.domain.ports.in.asignarinstructor.AsignarInstructorInputPort;
import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.out.ProgramacionAcademicaRepository;
import com.caeproject.cae.domain.ports.out.RapRepository;
import com.caeproject.cae.domain.ports.out.TrimestreRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class CrearProgramacionAcademicaUseCase implements CrearProgramacionInputPort {

    private final ProgramacionAcademicaRepository programacionAcademicaRepository;
    private final TrimestreRepository trimestreRepository;
    private final RapRepository rapRepository;
    private final AsignarInstructorInputPort asignarInstructorInputPort;

    public CrearProgramacionAcademicaUseCase(ProgramacionAcademicaRepository programacionAcademicaRepository, TrimestreRepository trimestreRepository, RapRepository rapRepository, AsignarInstructorInputPort asignarInstructorInputPort) {
        this.programacionAcademicaRepository = programacionAcademicaRepository;
        this.trimestreRepository = trimestreRepository;
        this.rapRepository = rapRepository;
        this.asignarInstructorInputPort = asignarInstructorInputPort;
    }

    @Override
    @Transactional
    public ProgramacionAcademica programacionAcademica(CrearProgramacionCommand command) {
        Rap rap = rapRepository.findById(command.getRapId())
                .orElseThrow(() -> new RapNoEncontradoException(command.getRapId()));
      trimestreRepository.findById(command.getTrimestreId())
                .orElseThrow(() -> new RuntimeException("No se encontró el trimestre con este id " + command.getTrimestreId()));


        AsignarInstructorCommand asignarCommand = new AsignarInstructorCommand(
                command.getUsuarioId(),
                command.getRapId(),
                command.getTrimestreId(),
                rap.getCompetenciaId(),
                command.getProgramaId()
        );

        asignarInstructorInputPort.asignarInstructor(asignarCommand);

        ProgramacionAcademica programacionAcademica = new ProgramacionAcademica();
        programacionAcademica.setTrimestreId(command.getTrimestreId());
        programacionAcademica.setRapId(command.getRapId());
        programacionAcademica.setUsuarioId(command.getUsuarioId());
        programacionAcademica.setProgramaId(command.getProgramaId());


        return programacionAcademicaRepository.saveProgramacion(programacionAcademica);

    }
}