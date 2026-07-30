package com.caeproject.cae.domain.ports.in.programacionacademica;

import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;

import java.util.List;

public interface ObtenerProgramacionInputPort {
    ProgramacionAcademica obtenerProgramacion(Long id);
    List<ProgramacionAcademica> findByUsuarioId(Long usuarioId);
    List<ProgramacionAcademica> findByTrimestreId(Long trimestreId);
}
