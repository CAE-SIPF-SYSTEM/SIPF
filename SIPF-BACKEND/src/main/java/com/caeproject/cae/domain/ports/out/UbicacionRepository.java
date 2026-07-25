package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.Departamento;
import com.caeproject.cae.domain.ports.model.Municipio;

import java.util.List;
import java.util.Optional;

public interface UbicacionRepository {
    void guardarTodos(List<Departamento> departamentos);

    boolean estaVacio();

    Optional<Municipio> obtenerMunicipioId(Long id);
    Optional<Municipio> obtenerMunicipioPorId(Long id);
    List<Departamento> obtenerDepartamentos();
}
