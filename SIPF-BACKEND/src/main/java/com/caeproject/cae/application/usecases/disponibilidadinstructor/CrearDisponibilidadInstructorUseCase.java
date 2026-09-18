    package com.caeproject.cae.application.usecases.disponibilidadinstructor;

    import com.caeproject.cae.application.usecases.disponibilidadinstructor.commands.CrearDisponibilidadCommand;
    import com.caeproject.cae.domain.ports.in.disponibilidadinstructor.CrearDisponibilidadInstructorInputPort;
    import com.caeproject.cae.domain.ports.model.DisponibilidadInstructor;
    import com.caeproject.cae.domain.ports.model.PerfilBase;
    import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;
    import com.caeproject.cae.domain.ports.out.DisponibilidadInstructorRepository;
    import com.caeproject.cae.domain.ports.out.PerfilBaseRepository;
    import com.caeproject.cae.domain.ports.out.UsuarioRepository;

    public class CrearDisponibilidadInstructorUseCase implements CrearDisponibilidadInstructorInputPort {

        private final DisponibilidadInstructorRepository disponibilidadInstructorRepository;
        private final UsuarioRepository usuarioRepository;
        private final PerfilBaseRepository perfilBaseRepository;

        public CrearDisponibilidadInstructorUseCase(DisponibilidadInstructorRepository disponibilidadInstructorRepository, UsuarioRepository usuarioRepository, PerfilBaseRepository perfilBaseRepository) {
            this.disponibilidadInstructorRepository = disponibilidadInstructorRepository;
            this.usuarioRepository = usuarioRepository;
            this.perfilBaseRepository = perfilBaseRepository;
        }

        @Override
        public DisponibilidadInstructor crearDisponibilidad(CrearDisponibilidadCommand command) {



            PerfilBase perfil = perfilBaseRepository.findById(command.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Perfil base no encontrado para el usuario: " + command.getUsuarioId()));
            DisponibilidadInstructor disponibilidad = new DisponibilidadInstructor();
            disponibilidad.setUsuarioId(command.getUsuarioId());
            disponibilidad.setDiasDisponibles(command.getDiasDisponibles());
            disponibilidad.setJornada(command.getJornada());
            disponibilidad.setMunicipios(command.getMunicipios());



            if (perfil.getTipoContrato() == TIpoContrato.PLANTA){
                disponibilidad.setHorasMaximas(144L);
            }
            else if (perfil.getTipoContrato()==TIpoContrato.CONTRATISTA){
                disponibilidad.setHorasMaximas(160L);
            }

            else {
                throw new IllegalStateException( "Contrato no disponible " + perfil.getTipoContrato());
            }
            return disponibilidadInstructorRepository.saveDisponibilidad(disponibilidad);
        }
    }
