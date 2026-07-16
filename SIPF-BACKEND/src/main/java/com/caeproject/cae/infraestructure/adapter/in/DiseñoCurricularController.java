package com.caeproject.cae.infraestructure.adapter.in;


import com.caeproject.cae.domain.ports.in.diseñoCurricular.EliminarDiseñoCurricularInputPort;
import com.caeproject.cae.domain.ports.in.diseñoCurricular.ListarDiseñoCurricularInputPort;
import com.caeproject.cae.domain.ports.model.DiseñoCurricular;
import com.caeproject.cae.infraestructure.dtos.dieñocurricular.DiseñoCurricularResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/diseñocurricular")
public class DiseñoCurricularController {

    private final ListarDiseñoCurricularInputPort listarDiseñoCurricularInputPort;
    private final EliminarDiseñoCurricularInputPort eliminarDiseñoCurricularInputPort;

    public DiseñoCurricularController(ListarDiseñoCurricularInputPort listarDiseñoCurricularInputPort, EliminarDiseñoCurricularInputPort eliminarDiseñoCurricularInputPort) {
        this.listarDiseñoCurricularInputPort = listarDiseñoCurricularInputPort;
        this.eliminarDiseñoCurricularInputPort = eliminarDiseñoCurricularInputPort;
    }

    @GetMapping
    public ResponseEntity<List<DiseñoCurricularResponseDTO>> listar(){
        List<DiseñoCurricular> lista = listarDiseñoCurricularInputPort.listarDiseñoCurricular();
        List<DiseñoCurricularResponseDTO> dtos = lista.stream().map(this::toResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{diseñocurricularId}")
    public ResponseEntity<Void> eliminarDiseñoCurricular(@PathVariable Long programaId){
        eliminarDiseñoCurricularInputPort.eliminarDiseñoCurricular(programaId);
        return ResponseEntity.ok().build();
    }


    private DiseñoCurricularResponseDTO toResponseDTO(DiseñoCurricular model) {
        DiseñoCurricularResponseDTO dto = new DiseñoCurricularResponseDTO();
        dto.setNumeroTrimestre(model.getNumeroTrimestre());
        dto.setProgramaId(model.getProgramaId());
        dto.setRapId(model.getRapId());
        return dto;
    }
}
