package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.AuditoriaPerfil;

public interface AuditoriaPerfilRepository {
    AuditoriaPerfil guardarAuditoria(AuditoriaPerfil auditoria);
    java.util.List<AuditoriaPerfil> obtenerPorUsuarioId(Long usuarioId);
    java.util.List<AuditoriaPerfil> listarTodas();
}
