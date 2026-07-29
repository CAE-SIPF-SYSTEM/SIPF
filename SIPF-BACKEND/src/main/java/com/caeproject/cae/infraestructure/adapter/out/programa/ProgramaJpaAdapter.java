package com.caeproject.cae.infraestructure.adapter.out.programa;

import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.domain.ports.out.ProgramaRepository;
import com.caeproject.cae.infraestructure.mappers.ProgramaMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProgramaJpaAdapter implements ProgramaRepository {

    private final ProgramaJpaRepository jpaRepository;
    private final ProgramaMapper mapper;

    public ProgramaJpaAdapter(ProgramaJpaRepository jpaRepository, ProgramaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Programa> findByNiveldeFormacion(NivelFormacion nivelFormacion) {
        return jpaRepository.findByNivelFormacion(nivelFormacion).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Programa> findByJornada(Jornada jornada) {
        return jpaRepository.findByJornada(jornada).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Programa> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Programa> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Programa> findByMunicipio(String nombre) {
        return jpaRepository.findByMunicipio_Nombre(nombre).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public boolean existByName(String name) {
        return jpaRepository.existsByNombre(name);
    }

    @Override
    public Programa savePrograma(Programa programa) {
        ProgramaEntity entity = mapper.toEntity(programa);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deletePrograma(Long id) {
        jpaRepository.deleteById(id);
    }
}
