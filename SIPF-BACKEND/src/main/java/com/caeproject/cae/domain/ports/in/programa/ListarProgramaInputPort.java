package com.caeproject.cae.domain.ports.in.programa;

import com.caeproject.cae.domain.ports.model.programa.Programa;

import java.util.List;

public interface ListarProgramaInputPort {
    List<Programa> listarProgramas();
}
