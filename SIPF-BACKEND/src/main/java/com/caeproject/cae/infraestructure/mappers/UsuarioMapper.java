package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import com.caeproject.cae.infraestructure.adapter.out.usuario.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PerfilBaseMapper.class})
public interface UsuarioMapper {
    @Mapping(target = "jwtToken", ignore = true)
    Usuario toDomain(UsuarioEntity entity);
    UsuarioEntity toEntity(Usuario domain);
}
