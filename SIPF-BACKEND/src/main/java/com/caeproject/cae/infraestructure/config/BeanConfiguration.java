package com.caeproject.cae.infraestructure.config;

import com.caeproject.cae.application.usecases.programacionacademica.AutoProgramarUseCase;
import com.caeproject.cae.application.usecases.programacionacademica.CrearProgramacionAcademicaUseCase;
import com.caeproject.cae.application.usecases.programacionacademica.EliminarProgramacionAcademicaUseCase;
import com.caeproject.cae.application.usecases.programacionacademica.ListarProgramacionAcademicaUseCase;
import com.caeproject.cae.application.usecases.programacionacademica.ObtenerProgramacionAcademicaUseCase;
import com.caeproject.cae.application.usecases.programacionacademica.ObtenerResumenFichaUseCase;
import com.caeproject.cae.application.usecases.programacionacademica.ObtenerResumenProgramaUseCase;
import com.caeproject.cae.application.usecases.asignacioninstructor.AsignacionInstructorUseCase;
import com.caeproject.cae.application.usecases.asignacioninstructor.SugerirInstructorUseCase;
import com.caeproject.cae.application.usecases.excel.AlimentacionCrUseCase;
import com.caeproject.cae.application.usecases.ubicacion.ConsultarUbicacionesUseCase;
import com.caeproject.cae.domain.ports.in.programacionacademica.*;
import com.caeproject.cae.domain.ports.in.programacionacademica.ObtenerResumenProgramaInputPort;
import com.caeproject.cae.domain.ports.in.asignarinstructor.AsignarInstructorInputPort;
import com.caeproject.cae.domain.ports.in.asignarinstructor.SugerirInstructorInputPort;
import com.caeproject.cae.domain.ports.in.ubicacion.ConsultarUbicacionesInputPort;
import com.caeproject.cae.domain.ports.out.*;
import com.caeproject.cae.domain.ports.service.ValidarElegibilidadInstructor;
import com.caeproject.cae.application.usecases.recuperacion.RestablecerContrasenaUseCase;
import com.caeproject.cae.application.usecases.recuperacion.SolicitarRecuperacionUseCase;
import com.caeproject.cae.application.usecases.usuario.*;
import com.caeproject.cae.domain.ports.in.recuperacion.RestablecerContrasenaInputPort;
import com.caeproject.cae.domain.ports.in.recuperacion.SolicitarRecuperacionInputPort;
import com.caeproject.cae.domain.ports.in.usuario.*;
import com.caeproject.cae.application.usecases.excel.ExportacionInstructorTrimestreUseCase;
import com.caeproject.cae.domain.ports.in.disponibilidadinstructor.ExportacionInstructorTrimestreInputPort;
import com.caeproject.cae.infraestructure.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BeanConfiguration {

    @Bean
    public CrearUsuarioInputPort crearUsuarioInputPort(UsuarioRepository usuarioRepository,
                                                       PerfilBaseRepository perfilBaseRepository) {
        return new CrearUsuarioUseCase(usuarioRepository, perfilBaseRepository);
    }

    @Bean
    public EditarUsuarioInputPort editarUsuarioInputPort(UsuarioRepository usuarioRepository, com.caeproject.cae.domain.ports.out.AuditoriaPerfilRepository auditoriaPerfilRepository) {
        return new EditarUsuarioUseCase(usuarioRepository, auditoriaPerfilRepository);
    }

    @Bean
    public EliminarUsuarioInputPort eliminarUsuarioInputPort(UsuarioRepository usuarioRepository) {
        return new EliminarUsuarioUseCase(usuarioRepository);
    }

    @Bean
    public LIstarUsuariosInputPort listarUsuariosInputPort(UsuarioRepository usuarioRepository) {
        return new LIstarUsuariosUseCase(usuarioRepository);
    }

    @Bean
    public ObtenerUsuarioInputPort obtenerUsuarioInputPort(UsuarioRepository usuarioRepository) {
        return new ObtenerUsuarioUseCase(usuarioRepository);
    }

    @Bean
    public InhabilitarUsuarioInputPort inhabilitarUsuarioInputPort(UsuarioRepository usuarioRepository) {
        return new InhabilitarUseCase(usuarioRepository);
    }

    @Bean
    public HabilitarUsuarioInputPort habilitarUsuarioInputPort(UsuarioRepository usuarioRepository) {
        return new HabilitarUseCase(usuarioRepository);
    }

    @Bean
    public com.caeproject.cae.domain.ports.in.usuario.LoginInputPort loginInputPort(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        return new com.caeproject.cae.application.usecases.usuario.LoginUseCase(usuarioRepository,jwtUtil);
    }

    @Bean
    public SolicitarRecuperacionInputPort solicitarRecuperacionInputPort(
            UsuarioRepository usuarioRepository,
            TokenRecuperacionRepository tokenRecuperacionRepository,
            EmailNotificationPort emailNotificationPort) {
        return new SolicitarRecuperacionUseCase(usuarioRepository, tokenRecuperacionRepository, emailNotificationPort);
    }

    @Bean
    public RestablecerContrasenaInputPort restablecerContrasenaInputPort(
            TokenRecuperacionRepository tokenRecuperacionRepository,
            UsuarioRepository usuarioRepository) {
        return new RestablecerContrasenaUseCase(tokenRecuperacionRepository, usuarioRepository);
    }

    // --- BEANS DE PROGRAMA ---
    @Bean
    public com.caeproject.cae.domain.ports.in.programa.CrearProgramaInputPort crearProgramaInputPort(com.caeproject.cae.domain.ports.out.ProgramaRepository programaRepository, UbicacionRepository ubicacionRepository) {
        return new com.caeproject.cae.application.usecases.programa.CrearProgramaUseCase(programaRepository, ubicacionRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.programa.ListarProgramaInputPort listarProgramaInputPort(com.caeproject.cae.domain.ports.out.ProgramaRepository programaRepository) {
        return new com.caeproject.cae.application.usecases.programa.ListarProgramaUseCase(programaRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.programa.ObtenerProgramaInputPort obtenerProgramaInputPort(com.caeproject.cae.domain.ports.out.ProgramaRepository programaRepository) {
        return new com.caeproject.cae.application.usecases.programa.ObtenerProgramaUseCase(programaRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.programa.EditarProgramaInputPort editarProgramaInputPort(com.caeproject.cae.domain.ports.out.ProgramaRepository programaRepository, UbicacionRepository ubicacionRepository) {
        return new com.caeproject.cae.application.usecases.programa.EditarProgramaUseCase(programaRepository, ubicacionRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.programa.EliminarProgramaInputPort eliminarProgramaInputPort(com.caeproject.cae.domain.ports.out.ProgramaRepository programaRepository) {
        return new com.caeproject.cae.application.usecases.programa.EliminarProgramaUseCase(programaRepository);
    }

    // --- BEANS DE FICHA ---
    @Bean
    public com.caeproject.cae.domain.ports.in.ficha.RegistrarFichaInputPort registrarFichaInputPort(
            com.caeproject.cae.domain.ports.out.FichaRepository fichaRepository,
            com.caeproject.cae.domain.ports.out.ProgramaRepository programaRepository) {
        return new com.caeproject.cae.application.usecases.ficha.CrearFichaUseCase(fichaRepository, programaRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.ficha.ListarFichasInputPort listarFichasInputPort(com.caeproject.cae.domain.ports.out.FichaRepository fichaRepository) {
        return new com.caeproject.cae.application.usecases.ficha.ListarFichasUseCase(fichaRepository);
    }

    @Bean
    public com.caeproject.cae.domain.ports.in.ficha.ObtenerAvanceFichasInputPort obtenerAvanceFichasInputPort(
            com.caeproject.cae.domain.ports.out.FichaRepository fichaRepository,
            com.caeproject.cae.domain.ports.out.ProgramaRepository programaRepository,
            com.caeproject.cae.domain.ports.out.ProgramacionAcademicaRepository programacionAcademicaRepository,
            com.caeproject.cae.domain.ports.out.DiseñoCurricularRepository diseñoCurricularRepository,
            com.caeproject.cae.domain.ports.out.RapRepository rapRepository) {
        return new com.caeproject.cae.application.usecases.ficha.ObtenerAvanceFichasUseCase(
                fichaRepository, programaRepository, programacionAcademicaRepository, diseñoCurricularRepository, rapRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.ficha.ObtenerFIchaInputPort obtenerFIchaInputPort(com.caeproject.cae.domain.ports.out.FichaRepository fichaRepository) {
        return new com.caeproject.cae.application.usecases.ficha.ObtenerFichaUseCase(fichaRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.ficha.EditarFichaInputPort editarFichaInputPort(
            com.caeproject.cae.domain.ports.out.FichaRepository fichaRepository,
            com.caeproject.cae.domain.ports.out.ProgramaRepository programaRepository) {
        return new com.caeproject.cae.application.usecases.ficha.EditarFichaUseCase(fichaRepository, programaRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.ficha.EliminarFichaInputPort eliminarFichaInputPort(com.caeproject.cae.domain.ports.out.FichaRepository fichaRepository) {
        return new com.caeproject.cae.application.usecases.ficha.EliminarFichaUseCase(fichaRepository);
    }

    @Bean
    public com.caeproject.cae.domain.ports.in.competencia.CrearCompetenciaInputPort crearCompetenciaInputPort(com.caeproject.cae.domain.ports.out.CompetenciaRepository competenciaRepository) {
        return new com.caeproject.cae.application.usecases.competencia.CrearCompetenciaUseCase(competenciaRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.competencia.EditarCompetenciaInputPort editarCompetenciaInputPort(com.caeproject.cae.domain.ports.out.CompetenciaRepository competenciaRepository) {
        return new com.caeproject.cae.application.usecases.competencia.EditarCompetenciaUseCase(competenciaRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.competencia.EliminarCompetenciaInputPort eliminarCompetenciaInputPort(com.caeproject.cae.domain.ports.out.CompetenciaRepository competenciaRepository) {
        return new com.caeproject.cae.application.usecases.competencia.EliminarCompetenciaUseCase(competenciaRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.competencia.ListarCompetenciasInputPort listarCompetenciasInputPort(com.caeproject.cae.domain.ports.out.CompetenciaRepository competenciaRepository) {
        return new com.caeproject.cae.application.usecases.competencia.ListarCompetenciasUseCase(competenciaRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.competencia.ObtenerCompetenciaInputPort obtenerCompetenciaInputPort(com.caeproject.cae.domain.ports.out.CompetenciaRepository competenciaRepository) {
        return new com.caeproject.cae.application.usecases.competencia.ObtenerCompetenciaUseCase(competenciaRepository);
    }


    @Bean
    public com.caeproject.cae.domain.ports.in.rap.CrearRapInputPort crearRapInputPort(com.caeproject.cae.domain.ports.out.RapRepository rapRepository) {
        return new com.caeproject.cae.application.usecases.rap.CrearRapUseCase(rapRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.rap.EditarRapInputPort editarRapInputPort(com.caeproject.cae.domain.ports.out.RapRepository rapRepository) {
        return new com.caeproject.cae.application.usecases.rap.EditarRapUseCase(rapRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.rap.EliminarRapInputPort eliminarRapInputPort(com.caeproject.cae.domain.ports.out.RapRepository rapRepository) {
        return new com.caeproject.cae.application.usecases.rap.EliminarRapUseCase(rapRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.rap.ListarRapsInputPort listarRapsInputPort(com.caeproject.cae.domain.ports.out.RapRepository rapRepository) {
        return new com.caeproject.cae.application.usecases.rap.ListarRapsUseCase(rapRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.rap.ObtenerRapInputPort obtenerRapInputPort(com.caeproject.cae.domain.ports.out.RapRepository rapRepository) {
        return new com.caeproject.cae.application.usecases.rap.ObtenerRapUseCase(rapRepository);
    }

    @Bean
    public AlimentacionCrUseCase alimentacionCrUseCase(
            AlimentacionCRRepository alimentacionCRRepository,
            CompetenciaRepository competenciaRepository,
            RapRepository rapRepository,
            DiseñoCurricularRepository disenoCurricularRepository) {
        return new AlimentacionCrUseCase(alimentacionCRRepository, competenciaRepository, rapRepository, disenoCurricularRepository);
    }

    // --- BEANS DE TRIMESTRE ---
    @Bean
    public com.caeproject.cae.domain.ports.in.trimestre.CrearTrimestreInputPort crearTrimestreInputPort(com.caeproject.cae.domain.ports.out.TrimestreRepository trimestreRepository) {
        return new com.caeproject.cae.application.usecases.trimestre.CrearTrimestreUseCase(trimestreRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.trimestre.EditarTrimestreInputPort editarTrimestreInputPort(com.caeproject.cae.domain.ports.out.TrimestreRepository trimestreRepository) {
        return new com.caeproject.cae.application.usecases.trimestre.EditarTrimestreUseCase(trimestreRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.trimestre.EliminarTrimestreInputPort eliminarTrimestreInputPort(com.caeproject.cae.domain.ports.out.TrimestreRepository trimestreRepository) {
        return new com.caeproject.cae.application.usecases.trimestre.EliminarTrimestreUseCase(trimestreRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.trimestre.ListarTrimestreInputPort listarTrimestreInputPort(com.caeproject.cae.domain.ports.out.TrimestreRepository trimestreRepository) {
        return new com.caeproject.cae.application.usecases.trimestre.ListarTrimestreUseCase(trimestreRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.trimestre.ObtenerTrimestreInputPort obtenerTrimestreInputPort(com.caeproject.cae.domain.ports.out.TrimestreRepository trimestreRepository) {
        return new com.caeproject.cae.application.usecases.trimestre.ObtenerTrimestreUseCase(trimestreRepository);
    }

    // --- BEANS DE ESPECIALIDAD ---
    @Bean
    public com.caeproject.cae.domain.ports.in.especialidad.CrearEspecialidadInputPort crearEspecialidadInputPort(com.caeproject.cae.domain.ports.out.EspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.especialidad.CrearEspecialidadUseCase(repository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.especialidad.EditarEspecialidadInputPort editarEspecialidadInputPort(com.caeproject.cae.domain.ports.out.EspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.especialidad.EditarEspecialidadUseCase(repository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.especialidad.EliminarEspecialidadInputPort eliminarEspecialidadInputPort(com.caeproject.cae.domain.ports.out.EspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.especialidad.EliminarEspecialidadUseCase(repository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.especialidad.ListarEspecialidadInputPort listarEspecialidadInputPort(com.caeproject.cae.domain.ports.out.EspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.especialidad.ListarEspecialidadUseCase(repository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.especialidad.ObtenerEspecialidadInputPort obtenerEspecialidadInputPort(com.caeproject.cae.domain.ports.out.EspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.especialidad.ObtenerEspecialidadUseCase(repository);
    }

    // --- BEANS DE INSTRUCTOR ESPECIALIDAD ---
    @Bean
    public com.caeproject.cae.domain.ports.in.instructorespecialidad.AsignarEspecialidadInstructorInputPort asignarEspecialidadInstructorInputPort(com.caeproject.cae.domain.ports.out.InstructorEspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.especialidadinstructor.AsignarEspecialidadInstructorUseCase(repository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.instructorespecialidad.EditarEspecialidadInstructorInputPort editarEspecialidadInstructorInputPort(com.caeproject.cae.domain.ports.out.InstructorEspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.especialidadinstructor.EditarEspecialidadInstructorUseCase(repository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.instructorespecialidad.DesasignarEspecialidadInstructorInputPort desasignarEspecialidadInstructorInputPort(com.caeproject.cae.domain.ports.out.InstructorEspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.especialidadinstructor.DesasignarEspecialidadInstructorUseCase(repository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.instructorespecialidad.ListarInstructoresEspecialidadesInputPort listarInstructoresEspecialidadesInputPort(com.caeproject.cae.domain.ports.out.InstructorEspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.especialidadinstructor.ListarInstructoresEspecialidadesUseCase(repository);
    }

    // --- BEANS DE COMPETENCIA ESPECIALIDAD ---
    @Bean
    public com.caeproject.cae.domain.ports.in.competenciaespecialidad.AsignarEspecialidadCompetenciaInputPort asignarEspecialidadCompetenciaInputPort(CompetenciaEspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.competenciaespecialidad.AsignarEspecialidadCompetenciaUseCase(repository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.competenciaespecialidad.EditarEspecialidadCompetenciaInputPort editarEspecialidadCompetenciaInputPort(CompetenciaEspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.competenciaespecialidad.EditarEspecialidadCompetenciaUseCase(repository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.competenciaespecialidad.DesasignarEspecialidadCompetenciaInputPort desasignarEspecialidadCompetenciaInputPort(CompetenciaEspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.competenciaespecialidad.DesasignarEspecialidadCompetenciaUseCase(repository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.competenciaespecialidad.ListarEspecialidadesCompetenciaInputPort listarEspecialidadesCompetenciaInputPort(CompetenciaEspecialidadRepository repository) {
        return new com.caeproject.cae.application.usecases.competenciaespecialidad.ListarEspecialidadesCompetenciaUseCase(repository);
    }

    // -- BEANS DE DISEÑOCURRICULAR --
    @Bean
    public com.caeproject.cae.domain.ports.in.disenocurricular.EliminarDiseñoCurricularInputPort eliminarDiseñoCurricularInputPort (DiseñoCurricularRepository disenoCurricularRepository){
        return new com.caeproject.cae.application.usecases.disenocurricular.EliminarDiseñoCurricularUseCase(disenoCurricularRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.disenocurricular.ListarDiseñoCurricularInputPort listarDiseñoCurricularInputPort (DiseñoCurricularRepository disenoCurricularRepository){
        return new com.caeproject.cae.application.usecases.disenocurricular.LIstarDiseñoCurricularUseCase(disenoCurricularRepository);
    }

    // --- DISPONIBILIDAD INSTRUCTOR ---
    @Bean
    public com.caeproject.cae.domain.ports.in.disponibilidadinstructor.CrearDisponibilidadInstructorInputPort crearDisponibilidadInstructorInputPort(
            DisponibilidadInstructorRepository disponibilidadInstructorRepository,
            UsuarioRepository usuarioRepository,
            PerfilBaseRepository perfilBaseRepository) {

        return new com.caeproject.cae.application.usecases.disponibilidadinstructor.CrearDisponibilidadInstructorUseCase(
                disponibilidadInstructorRepository,
                usuarioRepository,
                perfilBaseRepository
        );
    }

    @Bean
    public com.caeproject.cae.domain.ports.in.disponibilidadinstructor.EditarDisponibilidadInstructorInputPort editarDisponibilidadInstructorInputPort(
            DisponibilidadInstructorRepository disponibilidadInstructorRepository
    ){
        return new com.caeproject.cae.application.usecases.disponibilidadinstructor.EditarDisponibilidadInstructorUseCase(disponibilidadInstructorRepository);
    }

    @Bean
    public com.caeproject.cae.domain.ports.in.disponibilidadinstructor.EliminarDisponibilidadInputPort eliminarDisponibilidadInputPort(DisponibilidadInstructorRepository disponibilidadInstructorRepository){
        return new com.caeproject.cae.application.usecases.disponibilidadinstructor.EliminarDisponibilidadInstructorUseCase(disponibilidadInstructorRepository);
    }

    @Bean com.caeproject.cae.domain.ports.in.disponibilidadinstructor.ListarDisponibilidadInstructorInputPort listarDisponibilidadInstructorInputPort(DisponibilidadInstructorRepository disponibilidadInstructorRepository){
        return  new com.caeproject.cae.application.usecases.disponibilidadinstructor.ListarDisponibilidadInstructorUseCase(disponibilidadInstructorRepository);
    }

    @Bean com.caeproject.cae.domain.ports.in.disponibilidadinstructor.ObtenerDisponibilidadInstructorInputPort obtenerDisponibilidadInstructorInputPort (DisponibilidadInstructorRepository disponibilidadInstructorRepository){
        return new com.caeproject.cae.application.usecases.disponibilidadinstructor.ObtenerDisponibilidadInstructorUseCase(disponibilidadInstructorRepository);
    }


    // --- DOMAIN SERVICE ---
    @Bean
    public ValidarElegibilidadInstructor validarElegibilidadInstructor() {
        return new ValidarElegibilidadInstructor();
    }

    // --- PROGRAMACION ACADEMICA Y ASIGNACION ---
    @Bean
    public CrearProgramacionInputPort crearProgramacionInputPort(
            ProgramacionAcademicaRepository programacionAcademicaRepository,
            TrimestreRepository trimestreRepository,
            RapRepository rapRepository,
            AsignarInstructorInputPort asignarInstructorInputPort
    ) {
        return new CrearProgramacionAcademicaUseCase(
                programacionAcademicaRepository,
                trimestreRepository,
                rapRepository,
                asignarInstructorInputPort
        );
    }


    @Bean
    public ObtenerProgramacionInputPort obtenerProgramacionInputPort(
            ProgramacionAcademicaRepository programacionAcademicaRepository) {
        return new ObtenerProgramacionAcademicaUseCase(programacionAcademicaRepository);
    }

    @Bean
    public ListarProgramacionInputPort listarProgramacionInputPort(
            ProgramacionAcademicaRepository programacionAcademicaRepository) {
        return new ListarProgramacionAcademicaUseCase(programacionAcademicaRepository);
    }

    @Bean
    public EliminarProgramacionInputPort eliminarProgramacionInputPort(
            ProgramacionAcademicaRepository programacionAcademicaRepository) {
        return new EliminarProgramacionAcademicaUseCase(programacionAcademicaRepository);
    }

    @Bean
    public ConsultarUbicacionesInputPort consultarUbicacionesPort(UbicacionRepository ubicacionRepository) {
        return new ConsultarUbicacionesUseCase(ubicacionRepository);
    }

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public AsignarInstructorInputPort asignarInstructorInputPort(
            InstructorEspecialidadRepository instructorEspecialidadRepository,
            CompetenciaEspecialidadRepository competenciaEspecialidadRepository,
            DisponibilidadInstructorRepository disponibilidadInstructorRepository,
            ProgramaRepository programaRepository,
            DiseñoCurricularRepository disenoCurricularRepository,
            ValidarElegibilidadInstructor validarElegibilidadInstructor
    ) {
        return new AsignacionInstructorUseCase(
                instructorEspecialidadRepository,
                competenciaEspecialidadRepository,
                disponibilidadInstructorRepository,
                programaRepository,
                disenoCurricularRepository,
                validarElegibilidadInstructor
        );
    }

    @Bean
    public SugerirInstructorInputPort sugerirInstructorInputPort(
            ValidarElegibilidadInstructor validarElegibilidadInstructor,
            DisponibilidadInstructorRepository disponibilidadInstructorRepository,
            InstructorEspecialidadRepository instructorEspecialidadRepository,
            CompetenciaEspecialidadRepository competenciaEspecialidadRepository,
            ProgramaRepository programaRepository,
            FichaRepository fichaRepository
    ) {
        return new SugerirInstructorUseCase(
                validarElegibilidadInstructor,
                disponibilidadInstructorRepository,
                instructorEspecialidadRepository,
                competenciaEspecialidadRepository,
                programaRepository,
                fichaRepository
        );
    }

    @Bean
    public ObtenerResumenProgramaInputPort obtenerResumenProgramaInputPort(
            ProgramaRepository programaRepository,
            CompetenciaRepository competenciaRepository,
            DiseñoCurricularRepository disenoCurricularRepository,
            SugerirInstructorInputPort sugerirInstructorInputPort
    ) {
        return new ObtenerResumenProgramaUseCase(
                programaRepository,
                competenciaRepository,
                disenoCurricularRepository,
                sugerirInstructorInputPort
        );
    }

    @Bean
    public ObtenerResumenFichaInputPort obtenerResumenFichaInputPort(
            SugerirInstructorInputPort sugerirInstructorInputPort,
            FichaRepository fichaRepository,
            CompetenciaRepository competenciaRepository,
            DiseñoCurricularRepository disenoCurricularRepository
    ) {
        return new ObtenerResumenFichaUseCase(
                sugerirInstructorInputPort,
                fichaRepository,
                competenciaRepository,
                disenoCurricularRepository
        );
    }

    @Bean
    public AutoProgramacionAcademicaInputPort autoProgramacionAcademicaInputPort(
            SugerirInstructorInputPort sugerirInstructorInputPort,
            ProgramacionAcademicaRepository programacionAcademicaRepository,
            DisponibilidadInstructorRepository disponibilidadInstructorRepository,
            DiseñoCurricularRepository disenoCurricularRepository,
            RapRepository rapRepository,
            FichaRepository fichaRepository
    ) {
        return new AutoProgramarUseCase(
                sugerirInstructorInputPort,
                programacionAcademicaRepository,
                disponibilidadInstructorRepository,
                disenoCurricularRepository,
                rapRepository,
                fichaRepository
        );
    }

    @Bean
    public com.caeproject.cae.domain.ports.in.auditoria.ConsultarAuditoriaInputPort consultarAuditoriaInputPort(
            AuditoriaPerfilRepository auditoriaPerfilRepository) {
        return new com.caeproject.cae.application.usecases.auditorias.ConsultarAuditoriaUseCase(auditoriaPerfilRepository);
    }


    @Bean
    public ExportacionInstructorTrimestreInputPort exportacionInstructorTrimestreInputPort(
            ExportadorInstructorTrimestreRepository exportadorInstructorTrimestreRepository) {
        return new ExportacionInstructorTrimestreUseCase(exportadorInstructorTrimestreRepository);
    }
}
