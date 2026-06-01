package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.perfil_base.PerfilBase;
import com.caeproject.cae.infraestructure.adapter.out.perfil_base.PerfilBaseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PerfilBaseMapper {

    @Mapping(source = "documentoIdentidad", target = "cc")
    PerfilBase toDomain(PerfilBaseEntity entity);

    @Mapping(source = "cc", target = "documentoIdentidad")
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "new", ignore = true)
    PerfilBaseEntity toEntity(PerfilBase domain);
}
