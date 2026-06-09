package com.caeproject.cae.infraestructure.adapter.out.perfil_base;

import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;
import com.caeproject.cae.infraestructure.adapter.out.usuario.UsuarioEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "PERFIL_BASE")
public class PerfilBaseEntity implements Persistable<Long> {

    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(name = "documento_identidad", unique = true, nullable = false)
    private Long documentoIdentidad;

    private Long telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_contrato")
    private TIpoContrato tipoContrato;

    @Transient
    private boolean isNew = true;

    @Override
    public Long getId() {
        return usuarioId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public UsuarioEntity getUsuario() { return usuario; }
    public void setUsuario(UsuarioEntity usuario) { this.usuario = usuario; }

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
