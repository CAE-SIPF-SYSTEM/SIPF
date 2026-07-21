package com.caeproject.cae.application.usecases.ProgramacionAcademica;

import com.caeproject.cae.application.usecases.ProgramacionAcademica.commands.CrearProgramacionCommand;
import com.caeproject.cae.domain.ports.exceptions.competenciaespecialidadexception.CompetenciaEspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.competenciaexception.CompetenciaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.especialidadexception.EspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.FichaNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.fichasprogramasexception.ProgramaNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.instructorespecialidadexception.InstructorEspecialidadNoEncontradaException;
import com.caeproject.cae.domain.ports.exceptions.rapexception.RapNoEncontradoException;
import com.caeproject.cae.domain.ports.exceptions.usuarioexceptions.UsuarioNoEncontradoException;
import com.caeproject.cae.domain.ports.in.ProgramacionAcademica.CrearProgramacionInputPort;
import com.caeproject.cae.domain.ports.model.*;
import com.caeproject.cae.domain.ports.out.*;
import org.apache.commons.collections4.Trie;

public class CrearProgramacionAcademicaUseCase implements CrearProgramacionInputPort {

    private final ProgramacionAcademicaRepository programacionAcademicaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TrimestreRepository trimestreRepository;
    private final RapRepository rapRepository;
    private final DisponibilidadInstructorRepository disponibilidadInstructorRepository;
    private final DiseñoCurricularRepository diseñoCurricularRepository;
    private final ProgramaRepository programaRepository;
    private final FichaRepository fichaRepository;
    private final EspecialidadRepository especialidadRepository;
    private final InstructorEspecialidadRepository instructorEspecialidadRepository;
    private final CompetenciaEspecialidadRepository competenciaEspecialidadRepository;


    public CrearProgramacionAcademicaUseCase(ProgramacionAcademicaRepository programacionAcademicaRepository, UsuarioRepository usuarioRepository, TrimestreRepository trimestreRepository, RapRepository rapRepository, DisponibilidadInstructorRepository disponibilidadInstructorRepository, DiseñoCurricularRepository diseñoCurricularRepository, ProgramaRepository programaRepository, FichaRepository fichaRepository, EspecialidadRepository especialidadRepository, InstructorEspecialidadRepository instructorEspecialidadRepository, CompetenciaEspecialidadRepository competenciaEspecialidadRepository) {
        this.programacionAcademicaRepository = programacionAcademicaRepository;
        this.usuarioRepository = usuarioRepository;
        this.trimestreRepository = trimestreRepository;
        this.rapRepository = rapRepository;
        this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
        this.diseñoCurricularRepository = diseñoCurricularRepository;
        this.programaRepository = programaRepository;
        this.fichaRepository = fichaRepository;
        this.especialidadRepository = especialidadRepository;
        this.instructorEspecialidadRepository = instructorEspecialidadRepository;
        this.competenciaEspecialidadRepository = competenciaEspecialidadRepository;
    }

    //metodos de obtencion


    private DisponibilidadInstructor obtenerDisponiblidad (Long usuariooId){
        return disponibilidadInstructorRepository.findById(usuariooId)
                .orElseThrow(()-> new UsuarioNoEncontradoException(usuariooId));

    }
    private DiseñoCurricular obtenerDiseño  (Long rapId){
        return diseñoCurricularRepository.findByRapId(rapId)
                .findFirst()
                .orElseThrow(()-> new RapNoEncontradoException(rapId));
    }
    private Rap obtenerRap (Long id){
        return rapRepository.findById(id)
                .orElseThrow(()-> new RapNoEncontradoException(id));
    }

    private Rap ObtenerRapPorCompetencia (Long competenciaId){
        return rapRepository.findByCompetencia(competenciaId)
                .stream()
                .findFirst()
                .orElseThrow(()-> new RapNoEncontradoException(competenciaId));
    }



    private Trimestre obtenerTrimestre(Long trimestreId){
        return trimestreRepository.findById(trimestreId)
                .orElseThrow(()-> new RuntimeException("Trimestre no encontrado"));
    }

    private Ficha obtenerFicha(Long fichaId){
        return fichaRepository.findById(fichaId)
                .orElseThrow(() -> new FichaNoEncontradaException(fichaId));
    }

    private Programa obtenerPrograma (Long programaId){
        return programaRepository.findById(programaId)
                .stream()
                .findFirst()
                .orElseThrow(()-> new ProgramaNoEncontradoException(programaId));
    }
    private InstructorEspecialidad obtenerEspecialidadInstructor(Long usuarioId){
        return instructorEspecialidadRepository.findByInstructorId(usuarioId)
                .orElseThrow(()-> new UsuarioNoEncontradoException(usuarioId));
    }

    private CompetenciaEspecialidad obtenerComptenciaPorEspecialidad (Long competenciaId){
        return competenciaEspecialidadRepository.findByCompetenciaId(competenciaId)
                .orElseThrow(()-> new CompetenciaNoEncontradaException(competenciaId));
    }

    @Override
    public ProgramacionAcademica programacionAcademica(CrearProgramacionCommand crearProgramacionCommand) {



    // implementacion e iniciacion de objetos
        DisponibilidadInstructor disponibilidad = obtenerDisponiblidad(crearProgramacionCommand.getUsuarioId());
        DiseñoCurricular diseño = obtenerDiseño(crearProgramacionCommand.getRapId());
        Rap rap = obtenerRap(crearProgramacionCommand.getRapId());
        Rap competencia = ObtenerRapPorCompetencia(rap.getCompetenciaId());
        Trimestre trimestre = obtenerTrimestre(crearProgramacionCommand.getTrimstreId());
        Ficha ficha = obtenerFicha(trimestre.getFichaId());
        Programa programa = obtenerPrograma(ficha.getProgramaId());
        InstructorEspecialidad instructorEspecialidad = obtenerEspecialidadInstructor(crearProgramacionCommand.getUsuarioId());
        CompetenciaEspecialidad competenciaEspecialidad = obtenerComptenciaPorEspecialidad(competencia.getId());


        if (competenciaEspecialidad.getEspecialidadId() != instructorEspecialidad.getEspecialidadId()){
             throw  new RuntimeException("La especialidad requerida para la competencia y sus resultados de aprendizaje no puede ser asignada a este instructor");
        }

        if (disponibilidad.getJornada() != programa.getJornada()) {
            throw new RuntimeException("La jornada del instructor es diferente a la del programa");
        }

        if (disponibilidad.getDiasDisponibles() == null) {
            throw  new RuntimeException( "El instructor no tiene dias disponibles para realizar su jornada con el resultado de aprendizaje");
        }

        if (disponibilidad.getHorasMaximas() == 0){
            throw new RuntimeException("El instrucor no tiene horas asignadas");

        }







        //metodo de suma de horas totales segun la competencia y el numero de raps a ver dentro del programa
        Integer horasTotalesCompetencia = diseñoCurricularRepository.sumarHorasPorCompetenciaYPrograma(crearProgramacionCommand.getRapId(), ficha.getProgramaId());

        if (horasTotalesCompetencia == null) {
            horasTotalesCompetencia = 0;
        }



        //debemos tomar en cuenta lo siguiente si tiene la misma jornada que la jornada del programa es decir mañan y mañana entonces valdra si no tirara instructor no tiene la misma jornada para ser asignado luego
        //tendremos que valorar tambien si tiene horas disponibles para las horas necesarias de el resultado de aprendizaje o el total del volumen de la competencia por que usualmente es asi un instructor para todas estas y e aih la importancia que alguien de planta o contratista puede o tiene mas posibilidades  entregar las horas necesarias al trimestre




        return null;
    }
}
