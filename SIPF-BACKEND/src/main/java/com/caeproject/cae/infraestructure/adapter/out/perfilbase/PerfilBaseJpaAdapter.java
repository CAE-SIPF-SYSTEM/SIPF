package com.caeproject.cae.infraestructure.adapter.out.perfilbase;

import com.caeproject.cae.domain.ports.model.perfilbase.PerfilBase;
import com.caeproject.cae.domain.ports.out.PerfilBaseRepository;
import com.caeproject.cae.infraestructure.adapter.out.usuario.UsuarioEntity;
import com.caeproject.cae.infraestructure.adapter.out.usuario.UsuarioJpaRepository;
import com.caeproject.cae.infraestructure.mappers.PerfilBaseMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PerfilBaseJpaAdapter implements PerfilBaseRepository {

    private final PerfilBaseJpaRepository jpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final PerfilBaseMapper mapper;

    public PerfilBaseJpaAdapter(PerfilBaseJpaRepository jpaRepository,
                                UsuarioJpaRepository usuarioJpaRepository,
                                PerfilBaseMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<PerfilBase> findByCC(Long cc) {
        return jpaRepository.findByDocumentoIdentidad(cc).map(mapper::toDomain);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public PerfilBase save(PerfilBase perfilBase) {
        PerfilBaseEntity entity = mapper.toEntity(perfilBase);
        if (perfilBase.getUsuarioId() != null) {
            boolean exists = jpaRepository.existsById(perfilBase.getUsuarioId());
            entity.setNew(!exists);
        } else {
            entity.setNew(true);
        }
        UsuarioEntity usuarioRef = usuarioJpaRepository.findById(perfilBase.getUsuarioId()).orElseThrow();
        entity.setUsuario(usuarioRef);
        PerfilBaseEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
