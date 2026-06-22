package com.caeproject.cae.infraestructure.config;

import com.caeproject.cae.application.usecases.excel.AlimentacionCrUseCase;
import com.caeproject.cae.domain.ports.out.AlimentacionCRRepository;
import com.caeproject.cae.application.usecases.recuperacion.RestablecerContrasenaUseCase;
import com.caeproject.cae.application.usecases.recuperacion.SolicitarRecuperacionUseCase;
import com.caeproject.cae.application.usecases.usuario.*;
import com.caeproject.cae.domain.ports.in.recuperacion.RestablecerContrasenaInputPort;
import com.caeproject.cae.domain.ports.in.recuperacion.SolicitarRecuperacionInputPort;
import com.caeproject.cae.domain.ports.in.usuario.*;
import com.caeproject.cae.domain.ports.out.EmailNotificationPort;
import com.caeproject.cae.domain.ports.out.PerfilBaseRepository;
import com.caeproject.cae.domain.ports.out.TokenRecuperacionRepository;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
import com.caeproject.cae.domain.ports.out.CompetenciaRepository;
import com.caeproject.cae.domain.ports.out.RapRepository;
import com.caeproject.cae.infraestructure.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CrearUsuarioInputPort crearUsuarioInputPort(UsuarioRepository usuarioRepository,
                                                       PerfilBaseRepository perfilBaseRepository) {
        return new CrearUsuarioUseCase(usuarioRepository, perfilBaseRepository);
    }

    @Bean
    public EditarUsuarioInputPort editarUsuarioInputPort(UsuarioRepository usuarioRepository) {
        return new EditarUsuarioUseCase(usuarioRepository);
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
    public HabilitarUsuarioInputPort HabilitarUsuarioInputPort(UsuarioRepository usuarioRepository) {
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
    public com.caeproject.cae.domain.ports.in.programa.CrearProgramaInputPort crearProgramaInputPort(com.caeproject.cae.domain.ports.out.ProgramaRepository programaRepository) {
        return new com.caeproject.cae.application.usecases.programa.CrearProgramaUseCase(programaRepository);
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
    public com.caeproject.cae.domain.ports.in.programa.EditarProgramaInputPort editarProgramaInputPort(com.caeproject.cae.domain.ports.out.ProgramaRepository programaRepository) {
        return new com.caeproject.cae.application.usecases.programa.EditarProgramaUseCase(programaRepository);
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
    public com.caeproject.cae.domain.ports.in.Rap.CrearRapInputPort crearRapInputPort(com.caeproject.cae.domain.ports.out.RapRepository rapRepository) {
        return new com.caeproject.cae.application.usecases.rap.CrearRapUseCase(rapRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.Rap.EditarRapInputPort editarRapInputPort(com.caeproject.cae.domain.ports.out.RapRepository rapRepository) {
        return new com.caeproject.cae.application.usecases.rap.EditarRapUseCase(rapRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.Rap.EliminarRapInputPort eliminarRapInputPort(com.caeproject.cae.domain.ports.out.RapRepository rapRepository) {
        return new com.caeproject.cae.application.usecases.rap.EliminarRapUseCase(rapRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.Rap.ListarRapsInputPort listarRapsInputPort(com.caeproject.cae.domain.ports.out.RapRepository rapRepository) {
        return new com.caeproject.cae.application.usecases.rap.ListarRapsUseCase(rapRepository);
    }
    @Bean
    public com.caeproject.cae.domain.ports.in.Rap.ObtenerRapInputPort obtenerRapInputPort(com.caeproject.cae.domain.ports.out.RapRepository rapRepository) {
        return new com.caeproject.cae.application.usecases.rap.ObtenerRapUseCase(rapRepository);
    }

    @Bean
    public AlimentacionCrUseCase alimentacionCrUseCase(AlimentacionCRRepository alimentacionCRRepository,
                                                       CompetenciaRepository competenciaRepository,
                                                       RapRepository rapRepository) {
        return new AlimentacionCrUseCase(alimentacionCRRepository, competenciaRepository, rapRepository);
    }
}
