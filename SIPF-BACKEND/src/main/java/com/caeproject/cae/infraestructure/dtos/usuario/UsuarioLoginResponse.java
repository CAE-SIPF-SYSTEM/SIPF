package com.caeproject.cae.infraestructure.dtos.usuario;

import com.caeproject.cae.domain.ports.model.enums.Rol;

public class UsuarioLoginResponse {

    private Long id;
    private String correo;
    private Rol rol;
    private String tokenSession; //token para session unica

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getToken() { return tokenSession; }
    public void setToken(String token) { this.tokenSession = token; }
}
