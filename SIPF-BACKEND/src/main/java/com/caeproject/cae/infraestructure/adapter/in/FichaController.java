package com.caeproject.cae.infraestructure.adapter.in;

import com.caeproject.cae.application.usecases.ficha.commands.RegistrarFichaCommand;
import com.caeproject.cae.application.usecases.ficha.commands.EditarFichaCommand;
import com.caeproject.cae.domain.ports.in.ficha.*;
import com.caeproject.cae.domain.ports.model.Ficha;
import com.caeproject.cae.infraestructure.dtos.ficha.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fichas")
public class FichaController {

    private final RegistrarFichaInputPort registrarFichaPort;
    private final ListarFichasInputPort listarFichasPort;
    private final ObtenerFIchaInputPort obtenerFichaPort;
    private final EditarFichaInputPort editarFichaPort;
    private final EliminarFichaInputPort eliminarFichaPort;
    private final ObtenerAvanceFichasInputPort obtenerAvanceFichasPort;

    public FichaController(RegistrarFichaInputPort registrarFichaPort,
                           ListarFichasInputPort listarFichasPort,
                           ObtenerFIchaInputPort obtenerFichaPort,
                           EditarFichaInputPort editarFichaPort,
                           EliminarFichaInputPort eliminarFichaPort,
                           ObtenerAvanceFichasInputPort obtenerAvanceFichasPort) {
        this.registrarFichaPort = registrarFichaPort;
        this.listarFichasPort = listarFichasPort;
        this.obtenerFichaPort = obtenerFichaPort;
        this.editarFichaPort = editarFichaPort;
        this.eliminarFichaPort = eliminarFichaPort;
        this.obtenerAvanceFichasPort = obtenerAvanceFichasPort;
    }

    @GetMapping("/avance")
    public ResponseEntity<List<FichaAvanceResponse>> obtenerAvanceFichas() {
        return ResponseEntity.ok(obtenerAvanceFichasPort.obtenerAvanceFichas());
    }

    @PostMapping
    public ResponseEntity<FichaResponse> registrarFicha(@Valid @RequestBody RegistrarFichaRequest request) {
        RegistrarFichaCommand command = new RegistrarFichaCommand();
        command.setCodigoFicha(request.getCodigoFicha());
        command.setProgramaId(request.getProgramaId());
        command.setFechaInicio(request.getFechaInicio());
        command.setFechaFin(request.getFechaFin());

        Ficha registrada = registrarFichaPort.registrarFicha(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(registrada));
    }

    @GetMapping
    public ResponseEntity<List<FichaResponse>> listarFichas() {
        List<Ficha> fichas = listarFichasPort.listarFichas();
        return ResponseEntity.ok(fichas.stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FichaResponse> obtenerFicha(@PathVariable Long id) {
        Ficha ficha = obtenerFichaPort.obtenerFicha(id);
        return ResponseEntity.ok(toResponse(ficha));
    }

    @GetMapping("/programaid/{programaId}")
    public ResponseEntity<List<FichaResponse>> obtenerFichaPorProgramaId(@PathVariable Long programaId) {
        List<Ficha> fichas = obtenerFichaPort.obtenerFichaPorProgramaId(programaId);
        return ResponseEntity.ok(fichas.stream().map(this::toResponse).toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<FichaResponse> editarFicha(@PathVariable Long id, @Valid @RequestBody EditarFichaRequest request) {
        EditarFichaCommand command = new EditarFichaCommand();
        command.setCodigoFicha(request.getCodigoFicha());
        command.setProgramaId(request.getProgramaId());
        command.setFechaInicio(request.getFechaInicio());
        command.setFechaFin(request.getFechaFin());

        Ficha editada = editarFichaPort.editarFicha(command, id);
        return ResponseEntity.ok(toResponse(editada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFicha(@PathVariable Long id) {
        eliminarFichaPort.eliminarFicha(id);
        return ResponseEntity.noContent().build();
    }

    private FichaResponse toResponse(Ficha ficha) {
        FichaResponse response = new FichaResponse();
        response.setId(ficha.getId());
        response.setCodigoFicha(ficha.getCodigoFicha());
        response.setProgramaId(ficha.getProgramaId());
        response.setFechaInicio(ficha.getFechaInicio());
        response.setFechaFin(ficha.getFechaFin());
        return response;
    }
}
