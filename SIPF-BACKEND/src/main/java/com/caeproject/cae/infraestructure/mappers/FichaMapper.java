package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.Ficha;
import com.caeproject.cae.infraestructure.adapter.out.ficha.FichaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FichaMapper {
    Ficha toDomain(FichaEntity entity);

    @Mapping(target = "programa", ignore = true)
    FichaEntity toEntity(Ficha domain);
}
