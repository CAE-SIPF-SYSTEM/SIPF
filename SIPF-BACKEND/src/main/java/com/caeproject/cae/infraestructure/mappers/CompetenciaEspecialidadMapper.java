package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.CompetenciaEspecialidad;
import com.caeproject.cae.infraestructure.adapter.out.competenciaespecialidad.CompetenciaEspecialidadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CompetenciaEspecialidadMapper {

    CompetenciaEspecialidadMapper INSTANCE = Mappers.getMapper(CompetenciaEspecialidadMapper.class);

    CompetenciaEspecialidad toDomain(CompetenciaEspecialidadEntity entity);

    @Mapping(target = "competenciaId", source = "competenciaId")
    CompetenciaEspecialidadEntity toEntity(CompetenciaEspecialidad domain);
}
