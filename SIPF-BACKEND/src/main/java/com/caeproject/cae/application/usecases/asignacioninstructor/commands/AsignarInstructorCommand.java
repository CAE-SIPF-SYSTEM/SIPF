    package com.caeproject.cae.application.usecases.asignacioninstructor.commands;

    public record AsignarInstructorCommand(
            Long usuarioId,
            Long rapId,
            Long trimestreId,
            Long competenciaId,
            Long programaId) {

    }
