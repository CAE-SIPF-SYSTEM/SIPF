package com.caeproject.cae.domain.ports.in.programacionacademica;

import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;

import java.util.List;

public interface ListarProgramacionInputPort {
    List<ProgramacionAcademica>listarProgramaciones();
}
