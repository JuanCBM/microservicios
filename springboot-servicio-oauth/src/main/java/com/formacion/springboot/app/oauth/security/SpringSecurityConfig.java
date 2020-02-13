package com.formacion.springboot.app.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private AuthenticationEventPublisher eventPublisher;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	@Autowired // Para que se pueda inyectar.
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Personalizamos el userDetailsService y encriptamos la password con BCrypt
		auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder())
		// Registramos el evento de autenticación.
		.and().authenticationEventPublisher(eventPublisher);

	}

	// Lo anotamos para registrarlo en el contenedor de Spring y luego poder
	// utilizarlo para encriptar nuestras contraseñas.
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

}
