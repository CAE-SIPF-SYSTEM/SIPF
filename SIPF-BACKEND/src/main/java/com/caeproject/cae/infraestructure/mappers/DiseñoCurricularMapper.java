package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.infraestructure.adapter.out.diseñocurricular.DiseñoCurricularEntity;
import com.caeproject.cae.infraestructure.adapter.out.especialidad.EspecialidadEntity;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiseñoCurricularMapper {
    DiseñoCurricularEntity toEntity(DiseñoCurricular diseñoCurricular);

    DiseñoCurricular toDomain(DiseñoCurricularEntity entity);

    List<DiseñoCurricular> toDomainList(List<DiseñoCurricularEntity> entities);
}
