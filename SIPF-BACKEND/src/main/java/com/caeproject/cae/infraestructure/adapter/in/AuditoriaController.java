package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.domain.ports.in.auditoria.ConsultarAuditoriaInputPort;
import com.caeproject.cae.domain.ports.model.AuditoriaPerfil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
public class AuditoriaController {

    private final ConsultarAuditoriaInputPort consultarAuditoriaInputPort;

    public AuditoriaController(ConsultarAuditoriaInputPort consultarAuditoriaInputPort) {
        this.consultarAuditoriaInputPort = consultarAuditoriaInputPort;
    }

    @GetMapping
    public ResponseEntity<List<AuditoriaPerfil>> listarTodas() {
        return ResponseEntity.ok(consultarAuditoriaInputPort.listarTodas());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<AuditoriaPerfil>> obtenerPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(consultarAuditoriaInputPort.obtenerPorUsuarioId(usuarioId));
    }
}
