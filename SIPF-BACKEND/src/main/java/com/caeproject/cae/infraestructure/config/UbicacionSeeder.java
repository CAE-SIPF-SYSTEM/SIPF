package com.caeproject.cae.infraestructure.config;

import com.caeproject.cae.domain.ports.model.Departamento;
import com.caeproject.cae.domain.ports.model.Municipio;
import com.caeproject.cae.domain.ports.out.UbicacionRepository;
import com.caeproject.cae.infraestructure.adapter.out.api.ColombiaApiClientAdapter;
import com.caeproject.cae.infraestructure.dtos.api.ColombiaJsonDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UbicacionSeeder implements CommandLineRunner {

    private final ColombiaApiClientAdapter colombiaApiClientAdapter;
    private final UbicacionRepository ubicacionRepository;

    public UbicacionSeeder(ColombiaApiClientAdapter colombiaApiClientAdapter, UbicacionRepository ubicacionRepository) {
        this.colombiaApiClientAdapter = colombiaApiClientAdapter;
        this.ubicacionRepository = ubicacionRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        if (ubicacionRepository.estaVacio()){
            List<ColombiaJsonDto> dtos = colombiaApiClientAdapter.obtenerDatosDeApi(); //meotod que llama la api

            //datos a mapper
            List<Departamento> departamentos = dtos.stream()
                    .map(this::mapDtoToDomain)
                            .toList();
            //datos a db
            ubicacionRepository.guardarTodos(departamentos);
        }
    }
    private Departamento mapDtoToDomain (ColombiaJsonDto dto){
        Departamento departamento = new Departamento();
        departamento.setNombre(dto.getDepartamento());
        if (dto.getCiudades() != null) {
            List<Municipio> municipios = dto.getCiudades().stream()
                    .map(nombreCiudad -> {
                        Municipio municipio = new Municipio();
                        municipio.setNombre(nombreCiudad);
                        municipio.setDepartamento(departamento);
                        return municipio;
                    })
                    .toList();

            departamento.setMunicipios(municipios);
        }
        return departamento;
    }

}
