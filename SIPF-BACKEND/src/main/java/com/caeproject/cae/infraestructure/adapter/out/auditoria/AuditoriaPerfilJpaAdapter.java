package com.caeproject.cae.infraestructure.adapter.out.auditoria;

import com.caeproject.cae.domain.ports.model.AuditoriaPerfil;
import com.caeproject.cae.domain.ports.out.AuditoriaPerfilRepository;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaPerfilJpaAdapter implements AuditoriaPerfilRepository {

    private final AuditoriaPerfilJpaRepository jpaRepository;

    public AuditoriaPerfilJpaAdapter(AuditoriaPerfilJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public AuditoriaPerfil guardarAuditoria(AuditoriaPerfil auditoria) {
        AuditoriaPerfilEntity entity = new AuditoriaPerfilEntity();
        entity.setUsuarioId(auditoria.getUsuarioId());
        entity.setCampoModificado(auditoria.getCampoModificado());
        entity.setValorAnterior(auditoria.getValorAnterior());
        entity.setValorNuevo(auditoria.getValorNuevo());
        entity.setFechaModificacion(auditoria.getFechaModificacion());

        AuditoriaPerfilEntity saved = jpaRepository.save(entity);

        auditoria.setId(saved.getId());
        return auditoria;
    }

    @Override
    public java.util.List<AuditoriaPerfil> obtenerPorUsuarioId(Long usuarioId) {
        return jpaRepository.findByUsuarioIdOrderByFechaModificacionDesc(usuarioId).stream()
                .map(this::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public java.util.List<AuditoriaPerfil> listarTodas() {
        return jpaRepository.findAllByOrderByFechaModificacionDesc().stream()
                .map(this::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    private AuditoriaPerfil toDomain(AuditoriaPerfilEntity entity) {
        AuditoriaPerfil model = new AuditoriaPerfil();
        model.setId(entity.getId());
        model.setUsuarioId(entity.getUsuarioId());
        model.setCampoModificado(entity.getCampoModificado());
        model.setValorAnterior(entity.getValorAnterior());
        model.setValorNuevo(entity.getValorNuevo());
        model.setFechaModificacion(entity.getFechaModificacion());
        return model;
    }
}
