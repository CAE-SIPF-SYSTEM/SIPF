package com.caeproject.cae.infraestructure.dtos;

import com.caeproject.cae.domain.ports.model.enums.Rol;

public class EditarUsuarioRequest {

    private String correo;
    private String contrasena;
    private Rol rol;
    private Boolean estado;

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
