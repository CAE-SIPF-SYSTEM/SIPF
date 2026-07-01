package com.caeproject.cae.infraestructure.adapter.out.auditoria;

import com.caeproject.cae.domain.ports.model.auditoria.AuditoriaPerfil;
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
}
