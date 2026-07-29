package com.caeproject.cae.infraestructure.adapter.out.api;

import com.caeproject.cae.domain.ports.model.Departamento;
import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.out.UbicacionRepository;
import com.caeproject.cae.infraestructure.adapter.out.ubicacion.DepartamentoEntity;
import com.caeproject.cae.infraestructure.adapter.out.ubicacion.DepartamentoJPARepository;
import com.caeproject.cae.infraestructure.adapter.out.ubicacion.MunicipioJPARepository;
import com.caeproject.cae.infraestructure.mappers.UbicacionMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UbicacionPersistenceAdapter implements UbicacionRepository {

    private final DepartamentoJPARepository departamentoJPARepository;
    private final MunicipioJPARepository municipioJPARepository;
    private final UbicacionMapper ubicacionMapper;

    public UbicacionPersistenceAdapter(DepartamentoJPARepository departamentoJPARepository,
                                       MunicipioJPARepository municipioJPARepository,
                                       UbicacionMapper ubicacionMapper) {
        this.departamentoJPARepository = departamentoJPARepository;
        this.municipioJPARepository = municipioJPARepository;
        this.ubicacionMapper = ubicacionMapper;
    }

    @Override
    public void guardarTodos(List<Departamento> departamentos) {
        List<DepartamentoEntity> entities = ubicacionMapper.toEntityList(departamentos);
        // Asegurar que la relación bidireccional esté enlazada antes de guardar en cascada
        entities.forEach(dep -> {
            if (dep.getMunicipios() != null) {
                dep.getMunicipios().forEach(mun -> mun.setDepartamento(dep));
            }
        });
        departamentoJPARepository.saveAll(entities);
    }

    @Override
    public boolean estaVacio() {
        return departamentoJPARepository.count() == 0;
    }

    @Override
    public Optional<Municipio> obtenerMunicipioId(Long id) {
        return municipioJPARepository.findById(id)
                .map(ubicacionMapper::toDomain);
    }

    @Override
    public Optional<Municipio> obtenerMunicipioPorId(Long id) {
        return municipioJPARepository.findById(id)
                .map(ubicacionMapper::toDomain);

    }

    @Override
    public List<Departamento> obtenerDepartamentos() {
        return ubicacionMapper.toDomainDepartamentoList(departamentoJPARepository.findAll());
    }
}