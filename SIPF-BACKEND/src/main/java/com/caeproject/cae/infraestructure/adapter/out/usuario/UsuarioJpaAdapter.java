package com.caeproject.cae.infraestructure.adapter.out.usuario;

import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import com.caeproject.cae.infraestructure.mappers.UsuarioMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UsuarioJpaAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository jpaRepository;
    private final UsuarioMapper mapper;

    public UsuarioJpaAdapter(UsuarioJpaRepository jpaRepository, UsuarioMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return jpaRepository.findByCorreo(correo).map(mapper::toDomain);
    }

    @Override
    public List<Usuario> findByRol(Rol rol) {
        return jpaRepository.findByRol(rol).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> findByStatus(boolean estado) {
        return jpaRepository.findByEstado(estado).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteUser(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Usuario saveUser(Usuario usuario) {
        UsuarioEntity entity = mapper.toEntity(usuario);
        UsuarioEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
