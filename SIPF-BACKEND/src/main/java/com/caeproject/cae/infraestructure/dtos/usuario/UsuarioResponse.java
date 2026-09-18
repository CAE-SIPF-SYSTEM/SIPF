package com.caeproject.cae.infraestructure.dtos.usuario;

import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;

public class UsuarioResponse {

    private Long id;
    private String correo;
    private Rol rol;
    private boolean estado;
    private String nombre;
    private String apellido;
    private Long documentoIdentidad;
    private Long telefono;
    private TIpoContrato tipoContrato;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public Long getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(Long documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public Long getTelefono() { return telefono; }
    public void setTelefono(Long telefono) { this.telefono = telefono; }

    public TIpoContrato getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(TIpoContrato tipoContrato) { this.tipoContrato = tipoContrato; }
}
