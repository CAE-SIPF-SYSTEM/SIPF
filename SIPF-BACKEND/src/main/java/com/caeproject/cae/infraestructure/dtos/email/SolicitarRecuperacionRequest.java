package com.caeproject.cae.infraestructure.dtos.email;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitarRecuperacionRequest {

    @JsonAlias({"email", "correo"})
    private String correo;
}
