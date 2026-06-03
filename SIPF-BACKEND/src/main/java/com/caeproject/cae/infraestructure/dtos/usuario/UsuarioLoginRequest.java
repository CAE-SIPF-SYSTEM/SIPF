package com.caeproject.cae.infraestructure.dtos.usuario;

public class UsuarioLoginRequest {

    private String correo;
    private String contrasena;
    private String tokenSession; //token para session unica

    public String getTokenSession (){return tokenSession;}
    public void setTokenSession (String tokenSession) {this.tokenSession = tokenSession;}

    public String getCorreo() {return correo;}
    public void setCorreo(String correo) {this.correo = correo;}

    public String getContrasena() {return contrasena;}
    public void setContrasena(String contrasena) {this.contrasena = contrasena;}
}
