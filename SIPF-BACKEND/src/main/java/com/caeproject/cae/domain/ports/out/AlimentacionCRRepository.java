package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.competencia.Competencia;
import com.caeproject.cae.domain.ports.model.rap.Rap;
import java.io.InputStream;
import java.util.List;

public interface AlimentacionCRRepository {
    record CompetenciaRap (Competencia competencia, List<Rap> raps){}
    List<CompetenciaRap> extraerAlimentacion(InputStream alimentacionExcel);
}
