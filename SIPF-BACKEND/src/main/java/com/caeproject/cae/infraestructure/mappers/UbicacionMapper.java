package com.caeproject.cae.infraestructure.mappers;

import com.caeproject.cae.domain.ports.model.Departamento;
import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.infraestructure.adapter.out.ubicacion.DepartamentoEntity;
import com.caeproject.cae.infraestructure.adapter.out.ubicacion.MunicipioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UbicacionMapper{

    //departamento

    DepartamentoEntity toEntity(Departamento domain);
    List<DepartamentoEntity>toEntityList(List<Departamento> domainList);

    @Mapping(target = "municipios", ignore = true)
    Departamento toDomain(DepartamentoEntity entity);
    List<Departamento> toDomainDepartamentoList(List<DepartamentoEntity>entityList);

    //municipio
    @Mapping(target = "departamento", ignore = true)
    MunicipioEntity toEntity(Municipio domain);

    @Mapping(target = "departamento", ignore = true)
    Municipio toDomain(MunicipioEntity entity);

    List<Municipio> toDomainMunicipioList(List<MunicipioEntity> entityList);
}
