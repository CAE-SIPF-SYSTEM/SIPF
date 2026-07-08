package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.infraestructure.entities.EspecialidadEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EspecialidadMapper {

    EspecialidadEntity toEntity(Especialidad especialidad);

    Especialidad toDomain(EspecialidadEntity especialidadEntity);

    List<Especialidad> toDomainList(List<EspecialidadEntity> entities);
}
