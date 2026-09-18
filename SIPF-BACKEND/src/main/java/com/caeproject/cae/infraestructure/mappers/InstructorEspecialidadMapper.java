package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.InstructorEspecialidad;
import com.caeproject.cae.infraestructure.adapter.out.instructorespecialidad.InstructorEspecialidadEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InstructorEspecialidadMapper {
    InstructorEspecialidad toDomain(InstructorEspecialidadEntity entity);
    InstructorEspecialidadEntity toEntity(InstructorEspecialidad domain);
}
