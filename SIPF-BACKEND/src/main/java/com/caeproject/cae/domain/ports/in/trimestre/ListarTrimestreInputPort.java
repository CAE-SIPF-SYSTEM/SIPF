package com.caeproject.cae.domain.ports.in.trimestre;

import com.caeproject.cae.domain.ports.model.Trimestre;

import java.util.List;

public interface ListarTrimestreInputPort {
    List<Trimestre>listarTrimestres();
}
