package com.caeproject.cae.infraestructure.adapter.out;

import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.domain.ports.out.EspecialidadRepository;
import com.caeproject.cae.infraestructure.entities.EspecialidadEntity;
import com.caeproject.cae.infraestructure.mappers.EspecialidadMapper;
import com.caeproject.cae.infraestructure.repositories.JpaEspecialidadRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EspecialidadRepositoryAdapter implements EspecialidadRepository {

    private final JpaEspecialidadRepository jpaRepository;
    private final EspecialidadMapper mapper;

    public EspecialidadRepositoryAdapter(JpaEspecialidadRepository jpaRepository, EspecialidadMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Especialidad> findAll() {
        return mapper.toDomainList(jpaRepository.findAll());
    }

    @Override
    public Optional<Especialidad> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void eliminarEspecialidad(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Especialidad> findByNombre(String nombreEspecialidad) {
        return mapper.toDomainList(jpaRepository.findByNombreEspecialidadContainingIgnoreCase(nombreEspecialidad));
    }

    @Override
    public boolean existByNombre(String nombreEspecialidad) {
        return jpaRepository.existsByNombreEspecialidad(nombreEspecialidad);
    }

    @Override
    public Especialidad saveEspecialidad(Especialidad especialidad) {
        EspecialidadEntity entity = mapper.toEntity(especialidad);
        EspecialidadEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
