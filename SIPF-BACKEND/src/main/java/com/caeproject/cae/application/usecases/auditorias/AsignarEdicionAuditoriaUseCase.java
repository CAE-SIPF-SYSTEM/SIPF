package com.caeproject.cae.application.usecases.auditorias;

import com.caeproject.cae.application.usecases.auditorias.commands.AuditoriaCommand;
import com.caeproject.cae.domain.ports.in.auditoria.AuditoriaEdicionInputPort;
import com.caeproject.cae.domain.ports.model.AuditoriaPerfil;
import com.caeproject.cae.domain.ports.out.AuditoriaPerfilRepository;

public class AsignarEdicionAuditoriaUseCase implements AuditoriaEdicionInputPort {

    private final AuditoriaPerfilRepository auditoriaPerfilRepository;

    public AsignarEdicionAuditoriaUseCase(AuditoriaPerfilRepository auditoriaPerfilRepository) {
        this.auditoriaPerfilRepository = auditoriaPerfilRepository;
    }

    @Override
    public Long asignarEdicionAuditoria(AuditoriaCommand command) {
        AuditoriaPerfil auditoria = new AuditoriaPerfil();
        auditoria.setUsuarioId(command.getUsuarioId());
        auditoria.setCampoModificado(command.getCampoModificado());
        auditoria.setValorAnterior(command.getValorAnterior());
        auditoria.setValorNuevo(command.getValorNuevo());
        auditoria.setFechaModificacion(command.getFechaModificacion() != null ? command.getFechaModificacion() : java.time.LocalDateTime.now());
        
        AuditoriaPerfil guardado = auditoriaPerfilRepository.guardarAuditoria(auditoria);
        return guardado.getId();
    }
}
