package com.caeproject.cae.domain.ports.in.ubicacion;

import com.caeproject.cae.domain.ports.model.Departamento;
import com.caeproject.cae.domain.ports.model.Municipio;

import java.util.List;
import java.util.Optional;

public interface ConsultarUbicacionesInputPort {
    List<Departamento> obtenerTodosLosDepartamentos();
    List<Municipio> obtenerTodosLosMunicipios();
    Optional<Municipio> obtenerMunicipioPorId(Long id);
}
