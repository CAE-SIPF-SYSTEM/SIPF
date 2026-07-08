package com.caeproject.cae.domain.ports.model;


import com.caeproject.cae.domain.ports.model.enums.Rol;

public class Usuario {

    private Long id;
    private String correo;
    private String contrasena;
    private String tokenSession;
    private Rol rol;
    private boolean estado;

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getCorreo() {return correo;}

    public void setCorreo(String correo) {this.correo = correo;}

    public String getContrasena() {return contrasena;}

    public void setContrasena(String contrasena) {this.contrasena = contrasena;}

    public Rol getRol() {return rol;}

    public void setRol(Rol rol) {this.rol = rol;}

    public boolean isEstado() {return estado;}

    public void setEstado(boolean estado) {this.estado = estado;}

    public String getTokenSession (){return tokenSession;}
    public void setTokenSession(String tokenSession){this.tokenSession = tokenSession;}

    private String jwtToken;
    public String getJwtToken() { return jwtToken; }
    public void setJwtToken(String jwtToken) { this.jwtToken = jwtToken; }

    private PerfilBase perfilBase;
    public PerfilBase getPerfilBase() { return perfilBase; }
    public void setPerfilBase(PerfilBase perfilBase) { this.perfilBase = perfilBase; }
}
