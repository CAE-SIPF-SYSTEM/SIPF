package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.domain.ports.in.ubicacion.ConsultarUbicacionesInputPort;
import com.caeproject.cae.domain.ports.model.Departamento;
import com.caeproject.cae.domain.ports.model.Municipio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
public class UbicacionController {
    private final ConsultarUbicacionesInputPort consultarUbicacionesPort;

    public UbicacionController(ConsultarUbicacionesInputPort consultarUbicacionesPort) {
        this.consultarUbicacionesPort = consultarUbicacionesPort;
    }


    @GetMapping("/departamentos")
    public ResponseEntity<List<Departamento>> obtenerDepartamentos() {
        List<Departamento> departamentos = consultarUbicacionesPort.obtenerTodosLosDepartamentos();
        return ResponseEntity.ok(departamentos);
    }


    @GetMapping("/municipios/{id}")
    public ResponseEntity<Municipio> obtenerMunicipioPorId(@PathVariable Long id) {
        return consultarUbicacionesPort.obtenerMunicipioPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
