package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.Rap;
import com.caeproject.cae.infraestructure.adapter.out.rap.RapEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RapMapper {
    Rap toDomain(RapEntity entity);
    RapEntity toEntity(Rap domain);
}
