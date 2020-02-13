package com.formacion.springboot.app.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.formacion.springboot.app.commons.usuarios.models.entity.Usuario;
import com.formacion.springboot.app.oauth.services.IUsuarioService;

import feign.FeignException;

@Component
public class AutenticationSuccessErrorHandler implements AuthenticationEventPublisher {

	private Logger log = LoggerFactory.getLogger(AutenticationSuccessErrorHandler.class);

	@Autowired
	private IUsuarioService usuarioService;

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		UserDetails user = (UserDetails) authentication.getPrincipal();
		String mensaje = "Success Login: " + user.getUsername();
		System.out.println(mensaje);
		log.info(mensaje);
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		usuario.setIntentos(0);
		if (usuario.getIntentos() != null && usuario.getIntentos() > 0) {
			usuario.setIntentos(0);
			usuarioService.update(usuario, usuario.getId());
		}
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		UserDetails user = (UserDetails) authentication.getPrincipal();
		String mensaje = "Error en el login: " + user.getUsername() + " en: " + exception.getMessage();
		System.out.println(mensaje);
		log.info(mensaje);

		try {
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			if (usuario.getIntentos() != null) {
				log.info("Intentos actuales es de: " + usuario.getIntentos());
				usuario.setIntentos(usuario.getIntentos() + 1);
				log.info("Intentos después es de: " + usuario.getIntentos());

			} else {
				usuario.setIntentos(0);
			}

			if (usuario.getIntentos() >= 3) {
				log.error(String.format("Error con el usuario %s, ha sido deshabilitado", usuario.getUsername()));
				usuario.setEnabled(false);
			}

			usuarioService.update(usuario, usuario.getId());

		} catch (FeignException e) {
			log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
		}

	}

}
