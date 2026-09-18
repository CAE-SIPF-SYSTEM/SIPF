package com.caeproject.cae.domain.ports.in.auditoria;

import com.caeproject.cae.application.usecases.auditorias.commands.AuditoriaCommand;

public interface AuditoriaEdicionInputPort {
    Long asignarEdicionAuditoria (AuditoriaCommand command);
}
