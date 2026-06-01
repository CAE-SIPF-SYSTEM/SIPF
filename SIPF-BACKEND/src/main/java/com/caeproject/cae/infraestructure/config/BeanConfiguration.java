package com.caeproject.cae.infraestructure.config;

import com.caeproject.cae.application.usecases.usuario.CrearUsuarioUseCase;
import com.caeproject.cae.application.usecases.usuario.EditarUsuarioUseCase;
import com.caeproject.cae.application.usecases.usuario.EliminarUsuarioUseCase;
import com.caeproject.cae.application.usecases.usuario.LIstarUsuariosUseCase;
import com.caeproject.cae.application.usecases.usuario.ObtenerUsuarioUseCase;
import com.caeproject.cae.domain.ports.in.usuario.CrearUsuarioInputPort;
import com.caeproject.cae.domain.ports.in.usuario.EditarUsuarioInputPort;
import com.caeproject.cae.domain.ports.in.usuario.EliminarUsuarioInputPort;
import com.caeproject.cae.domain.ports.in.usuario.LIstarUsuariosInputPort;
import com.caeproject.cae.domain.ports.in.usuario.ObtenerUsuarioInputPort;
import com.caeproject.cae.domain.ports.out.PerfilBaseRepository;
import com.caeproject.cae.domain.ports.out.UsuarioRepository;
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
}
