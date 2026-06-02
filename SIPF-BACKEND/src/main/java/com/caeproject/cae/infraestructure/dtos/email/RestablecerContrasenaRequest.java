package com.caeproject.cae.infraestructure.dtos.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestablecerContrasenaRequest {

    private String token;
    private String nuevaContrasena;
}
