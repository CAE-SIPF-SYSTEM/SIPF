package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.auditoria.AuditoriaPerfil;

public interface AuditoriaPerfilRepository {
    AuditoriaPerfil guardarAuditoria(AuditoriaPerfil auditoria);
}
