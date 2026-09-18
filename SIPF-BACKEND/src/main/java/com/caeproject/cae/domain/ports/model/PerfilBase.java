package com.caeproject.cae.domain.ports.model;

import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;

public class PerfilBase {

    private String nombre;
    private String apellido;
    private Long cc;
    private Long telefono;

    private TIpoContrato tipoContrato;
    private Long usuarioId;

    public Long getUsuarioId() {return usuarioId;}

    public void setUsuarioId(Long usuarioId) {this.usuarioId = usuarioId;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getApellido() {return apellido;}

    public void setApellido(String apellido) {this.apellido = apellido;}

    public Long getCc() {return cc;}

    public void setCc(Long cc) {this.cc = cc;}

    public Long getTelefono() {return telefono;}

    public void setTelefono(Long telefono) {this.telefono = telefono;}

    public TIpoContrato getTipoContrato() {return tipoContrato;}

    public void setTipoContrato(TIpoContrato tipoContrato) {this.tipoContrato = tipoContrato;}
}
