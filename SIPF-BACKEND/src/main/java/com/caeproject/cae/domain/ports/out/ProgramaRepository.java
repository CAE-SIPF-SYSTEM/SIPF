package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.enums.Jornada;
import com.caeproject.cae.domain.ports.model.enums.NivelFormacion;
import com.caeproject.cae.domain.ports.model.Programa;

import java.util.List;
import java.util.Optional;

public interface ProgramaRepository {
    List<Programa> findByNiveldeFormacion(NivelFormacion nivelFormacion);
    List<Programa>findByJornada(Jornada jornada);
    Optional<Programa> findById (Long id);
    List <Programa> findAll();
    List <Programa> findByMunicipio(String municipio);
    boolean existByName (String name);
    Programa savePrograma (Programa programa);
    void deletePrograma (Long id);
}
