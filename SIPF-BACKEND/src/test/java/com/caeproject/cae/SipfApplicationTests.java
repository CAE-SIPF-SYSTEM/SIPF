package com.caeproject.cae;

import com.caeproject.cae.domain.ports.in.usuario.CrearUsuarioInputPort;
import com.caeproject.cae.domain.ports.model.enums.Rol;
import com.caeproject.cae.domain.ports.model.enums.TIpoContrato;
import com.caeproject.cae.domain.ports.model.perfil_base.PerfilBase;
import com.caeproject.cae.domain.ports.model.usuario.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SipfApplicationTests {

	@Autowired
	private CrearUsuarioInputPort crearUsuarioInputPort;

	@Test
	void contextLoads() {
	}

	@Test
	void testCrearUsuarioExito() {
		Usuario usuario = new Usuario();
		usuario.setCorreo("instructor@sena.edu.co");
		usuario.setContrasena("123456");
		usuario.setRol(Rol.INSTRUCTOR);
		usuario.setEstado(true);

		PerfilBase perfilBase = new PerfilBase();
		perfilBase.setNombre("Juan");
		perfilBase.setApellido("Pérez");
		perfilBase.setCc(1234567890L);
		perfilBase.setTelefono((short) 3001);
		perfilBase.setTipoContrato(TIpoContrato.PLANTA);

		Usuario creado = crearUsuarioInputPort.crearUsuario(usuario, perfilBase);

		assertNotNull(creado);
		assertNotNull(creado.getId());
		assertEquals("instructor@sena.edu.co", creado.getCorreo());
	}
}
