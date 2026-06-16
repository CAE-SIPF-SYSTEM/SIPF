package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.programa.Programa;
import com.caeproject.cae.infraestructure.adapter.out.programa.ProgramaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgramaMapper {
    Programa toDomain(ProgramaEntity entity);
    ProgramaEntity toEntity(Programa domain);
}
