package com.caeproject.cae.infraestructure.dtos.usuario;

import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;

public class CrearUsuarioRequest {

    private String correo;
    private String contrasena;
    private Rol rol;
    private String nombre;
    private String apellido;
    private Long documentoIdentidad;
    private Short telefono;
    private TIpoContrato tipoContrato;

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public Long getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(Long documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public Short getTelefono() { return telefono; }
    public void setTelefono(Short telefono) { this.telefono = telefono; }

    public TIpoContrato getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(TIpoContrato tipoContrato) { this.tipoContrato = tipoContrato; }
}
