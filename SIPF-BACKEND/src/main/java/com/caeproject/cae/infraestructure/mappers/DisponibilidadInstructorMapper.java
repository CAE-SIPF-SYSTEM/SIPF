package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;

import com.caeproject.cae.infraestructure.adapter.out.disponibilidadinstructor.DisponibilidadInstructorEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UbicacionMapper.class})
public interface DisponibilidadInstructorMapper {
    DisponibilidadInstructor toDomain(DisponibilidadInstructorEntity entity);
    DisponibilidadInstructorEntity toEntity(DisponibilidadInstructor domain);

}
