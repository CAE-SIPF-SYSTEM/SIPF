package com.caeproject.cae.domain.ports.in.auditoria;

import com.caeproject.cae.domain.ports.model.AuditoriaPerfil;
import java.util.List;

public interface ConsultarAuditoriaInputPort {
    List<AuditoriaPerfil> obtenerPorUsuarioId(Long usuarioId);
    List<AuditoriaPerfil> listarTodas();
}
