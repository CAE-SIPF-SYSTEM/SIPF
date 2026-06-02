package com.caeproject.cae.domain.ports.in.recuperacion;

public interface RestablecerContrasenaInputPort {

    void restablecerContrasena(String token, String nuevaContrasena);
}
