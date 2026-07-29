package com.caeproject.cae.infraestructure.dtos.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class    ColombiaJsonDto {
        @JsonProperty("id")
        @NotNull(message = "Nombre del departamento")
        Long id;

        @JsonProperty("departamento")
        String departamento;

        @JsonProperty("ciudades")
        List<String> ciudades;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getDepartamento() {
                return departamento;
        }

        public void setDepartamento(String departamento) {
                this.departamento = departamento;
        }

        public List<String> getCiudades() {
                return ciudades;
        }

        public void setCiudades(List<String> ciudades) {
                this.ciudades = ciudades;
        }
}
