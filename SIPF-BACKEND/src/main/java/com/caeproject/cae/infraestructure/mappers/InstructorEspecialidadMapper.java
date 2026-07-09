package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;
import com.caeproject.cae.infraestructure.entities.InstructorEspecialidadEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstructorEspecialidadMapper {
    InstructorEspecialidad toDomain(InstructorEspecialidadEntity entity);
    InstructorEspecialidadEntity toEntity(InstructorEspecialidad domain);
}
