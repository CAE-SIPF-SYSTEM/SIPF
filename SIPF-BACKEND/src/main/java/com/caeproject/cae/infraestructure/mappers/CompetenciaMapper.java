package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.competencia.Competencia;
import com.caeproject.cae.infraestructure.adapter.out.competencia.CompetenciaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompetenciaMapper {
    Competencia toDomain(CompetenciaEntity entity);
    CompetenciaEntity toEntity(Competencia domain);
}
