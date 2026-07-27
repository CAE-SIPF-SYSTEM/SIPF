package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.Programa;
import com.caeproject.cae.infraestructure.adapter.out.programa.ProgramaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UbicacionMapper.class})
public interface ProgramaMapper {
    Programa toDomain(ProgramaEntity entity);
    ProgramaEntity toEntity(Programa domain);
}
