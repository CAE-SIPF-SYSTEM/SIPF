package com.caeproject.cae.infraestructure.adapter.out.rap;

import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.domain.ports.out.RapRepository;
import com.caeproject.cae.infraestructure.mappers.RapMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RapJpaAdapter implements RapRepository {

    private final RapJpaRepository jpaRepository;
    private final RapMapper mapper;

    public RapJpaAdapter(RapJpaRepository jpaRepository, RapMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Rap saveRap(Rap rap) {
        RapEntity entity = mapper.toEntity(rap);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Rap> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Rap> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Rap> findByCompetencia(Long competenciaId) {
        return jpaRepository.findByCompetenciaId(competenciaId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarRap(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existByCompetenciaId(Long competenciaId) {
        return jpaRepository.existsByCompetenciaId(competenciaId);
    }
}
