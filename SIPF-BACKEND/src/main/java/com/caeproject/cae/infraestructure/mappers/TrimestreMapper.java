package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.Trimestre;
import com.caeproject.cae.infraestructure.adapter.out.trimestre.TrimestreEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrimestreMapper {
    Trimestre toDomain(TrimestreEntity entity);
    TrimestreEntity toEntity(Trimestre domain);
}
