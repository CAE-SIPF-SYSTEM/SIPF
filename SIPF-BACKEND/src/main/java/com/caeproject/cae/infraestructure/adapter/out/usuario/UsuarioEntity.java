package com.caeproject.cae.infraestructure.adapter.out.usuario;

import com.caeproject.cae.domain.ports.model.enums.Rol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USUARIO")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    @Column(name = "tokenSession", length = 36)
    private String tokenSession; //token de session


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(nullable = false)
    private boolean estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public String getTokenSession (){return tokenSession;}
    public void setTokenSession(String tokenSession){this.tokenSession = tokenSession;}

    @jakarta.persistence.OneToOne(mappedBy = "usuario", cascade = jakarta.persistence.CascadeType.ALL, fetch = jakarta.persistence.FetchType.EAGER)
    private com.caeproject.cae.infraestructure.adapter.out.perfilbase.PerfilBaseEntity perfilBase;

    public com.caeproject.cae.infraestructure.adapter.out.perfilbase.PerfilBaseEntity getPerfilBase() { return perfilBase; }
    public void setPerfilBase(com.caeproject.cae.infraestructure.adapter.out.perfilbase.PerfilBaseEntity perfilBase) { this.perfilBase = perfilBase; }
}
