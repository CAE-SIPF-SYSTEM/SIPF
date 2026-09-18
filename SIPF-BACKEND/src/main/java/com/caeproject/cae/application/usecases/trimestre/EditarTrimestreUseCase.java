package com.caeproject.cae.application.usecases.trimestre;

import com.caeproject.cae.application.usecases.trimestre.commands.EditarTrimestreCommand;
import com.caeproject.cae.domain.ports.in.trimestre.EditarTrimestreInputPort;
import com.caeproject.cae.domain.ports.model.Trimestre;
import com.caeproject.cae.domain.ports.out.TrimestreRepository;

public class EditarTrimestreUseCase implements EditarTrimestreInputPort {
    private final TrimestreRepository trimestreRepository;
    public EditarTrimestreUseCase (TrimestreRepository trimestreRepository) {
        this.trimestreRepository = trimestreRepository;
    }


    @Override
    public Trimestre editarTrimestre(EditarTrimestreCommand command, Long id) {
        Trimestre trimestreexist = trimestreRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Trimestre no encontrado"));
        if (command.getFechaInicio() != null ){
            trimestreexist.setFechaInicio(command.getFechaInicio());
        }
        if (command.getFechaFin() != null ){
            trimestreexist.setFechaFin(command.getFechaFin());
        }
        return trimestreRepository.savetrimestre(trimestreexist);
    }
}
