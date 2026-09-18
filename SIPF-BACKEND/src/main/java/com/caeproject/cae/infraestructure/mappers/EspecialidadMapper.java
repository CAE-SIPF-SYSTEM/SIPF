package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.Especialidad;
import com.caeproject.cae.infraestructure.adapter.out.especialidad.EspecialidadEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EspecialidadMapper {

    EspecialidadEntity toEntity(Especialidad especialidad);

    Especialidad toDomain(EspecialidadEntity especialidadEntity);

    List<Especialidad> toDomainList(List<EspecialidadEntity> entities);
}
