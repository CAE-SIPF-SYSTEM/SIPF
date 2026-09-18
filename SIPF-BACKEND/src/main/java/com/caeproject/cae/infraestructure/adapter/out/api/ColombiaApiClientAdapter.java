package com.caeproject.cae.infraestructure.adapter.out.api;

import com.caeproject.cae.infraestructure.dtos.api.ColombiaJsonDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Component
public class ColombiaApiClientAdapter {

    private static final String URL_COLOMBIA_JSON =
            "https://raw.githubusercontent.com/marcovega/colombia-json/master/colombia.min.json";

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ColombiaApiClientAdapter(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public List<ColombiaJsonDto>obtenerDatosDeApi(){
        try {
            String jsonText = restClient.get()
                    .uri(URL_COLOMBIA_JSON)
                    .retrieve()
                    .body(String.class);

            if (jsonText == null || jsonText.isBlank()) {
                return List.of();
            }
            ColombiaJsonDto[] respuesta = objectMapper.readValue(jsonText, ColombiaJsonDto[].class); //convierte texto a arreglo
            return Arrays.asList(respuesta);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al parsear el JSON de Colombia desde la API externa", e);
        }
    }
}
