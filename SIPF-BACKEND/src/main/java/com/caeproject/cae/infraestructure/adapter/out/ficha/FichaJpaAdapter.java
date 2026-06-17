package com.caeproject.cae.infraestructure.adapter.out.ficha;

import com.caeproject.cae.domain.ports.model.ficha.Ficha;
import com.caeproject.cae.domain.ports.out.FichaRepository;
import com.caeproject.cae.infraestructure.mappers.FichaMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FichaJpaAdapter implements FichaRepository {

    private final FichaJpaRepository jpaRepository;
    private final FichaMapper mapper;

    public FichaJpaAdapter(FichaJpaRepository jpaRepository, FichaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existByCodigoFicha(String codigoFicha) {
        return jpaRepository.existsByCodigoFicha(codigoFicha);
    }

    @Override
    public Ficha saveFicha(Ficha ficha) {
        FichaEntity entity = mapper.toEntity(ficha);
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Ficha> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Ficha> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void deleteFicha(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Ficha> findByProgramaId(Long programaId) {
        return jpaRepository.findByProgramaId(programaId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }
}
