package com.caeproject.cae.application.usecases.auditorias;

import com.caeproject.cae.domain.ports.in.auditoria.ConsultarAuditoriaInputPort;
import com.caeproject.cae.domain.ports.model.AuditoriaPerfil;
import com.caeproject.cae.domain.ports.out.AuditoriaPerfilRepository;
import java.util.List;

public class ConsultarAuditoriaUseCase implements ConsultarAuditoriaInputPort {

    private final AuditoriaPerfilRepository auditoriaPerfilRepository;

    public ConsultarAuditoriaUseCase(AuditoriaPerfilRepository auditoriaPerfilRepository) {
        this.auditoriaPerfilRepository = auditoriaPerfilRepository;
    }

    @Override
    public List<AuditoriaPerfil> obtenerPorUsuarioId(Long usuarioId) {
        return auditoriaPerfilRepository.obtenerPorUsuarioId(usuarioId);
    }

    @Override
    public List<AuditoriaPerfil> listarTodas() {
        return auditoriaPerfilRepository.listarTodas();
    }
}
