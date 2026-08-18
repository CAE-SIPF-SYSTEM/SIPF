package com.caeproject.cae.application.usecases.ubicacion;

import com.caeproject.cae.domain.ports.in.ubicacion.ConsultarUbicacionesInputPort;
import com.caeproject.cae.domain.ports.model.Departamento;
import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.out.UbicacionRepository;

import java.util.List;
import java.util.Optional;

public class ConsultarUbicacionesUseCase implements ConsultarUbicacionesInputPort {

    private final UbicacionRepository ubicacionRepository;

    public ConsultarUbicacionesUseCase(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @Override
    public List<Departamento> obtenerTodosLosDepartamentos() {
        return ubicacionRepository.obtenerDepartamentos();
    }

    @Override
    public List<Municipio> obtenerTodosLosMunicipios() {
        return ubicacionRepository.obtenerMunicipios();
    }

    @Override
    public Optional<Municipio> obtenerMunicipioPorId(Long id) {
        return ubicacionRepository.obtenerMunicipioPorId(id);
    }
}
