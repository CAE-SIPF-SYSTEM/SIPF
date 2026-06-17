package com.caeproject.cae.domain.ports.in.programa;

import com.caeproject.cae.domain.ports.model.programa.Programa;

public interface EditarProgramaInputPort {
    Programa editarPrograma (Programa programa, Long id);
}
