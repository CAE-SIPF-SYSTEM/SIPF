package com.caeproject.cae.domain.ports.out;

import com.caeproject.cae.domain.ports.model.Competencia;
import com.caeproject.cae.domain.ports.model.Rap;
import java.io.InputStream;
import java.util.List;

public interface AlimentacionCRRepository {
    record RapImport(Rap rap, Integer horasPresenciales){}
    record CompetenciaRap (Competencia competencia, List<RapImport> raps, Integer trimestre){}
    List<CompetenciaRap> extraerAlimentacion(InputStream alimentacionExcel);
}
