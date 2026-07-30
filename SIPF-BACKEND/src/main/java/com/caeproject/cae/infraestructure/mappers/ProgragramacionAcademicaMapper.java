package com.caeproject.cae.infraestructure.mappers;
import com.caeproject.cae.domain.ports.model.ProgramacionAcademica;
import com.caeproject.cae.infraestructure.adapter.out.programacionacademica.ProgramacionAcademicaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProgragramacionAcademicaMapper {
    ProgramacionAcademica toDomain(ProgramacionAcademicaEntity entity);
    ProgramacionAcademicaEntity toEntity(ProgramacionAcademica domain);

}
