package com.formacion.springboot.app.zuul.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@RefreshScope
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value("${config.security.oauth.jwt.key}")
	private String jwtKey;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/productos/listar", "/api/items/listar", "/api/usuarios/usuarios")
				.permitAll()
				.antMatchers(HttpMethod.GET, "/api/productos/ver/{id}", "/api/items/ver/{id}/cantidad/{cantidad}",
						"/api/usuarios/usuarios/{id}")
				.hasAnyRole("ADMIN", "USER").antMatchers("/api/productos/**", "/api/items/**", "/api/usuarios/**")
				.hasRole("ADMIN").anyRequest().authenticated();

//								.antMatchers(HttpMethod.POST, "/api/productos/crear", "/api/items/crear").hasRole("ADMIN")
//								.antMatchers(HttpMethod.PUT, "/api/productos/editar/{id}", "/api/items/crear/{id}",
//											"/api/usuarios/usuarios/{id}").hasRole("ADMIN")
//								.antMatchers(HttpMethod.DELETE, "/api/productos/eliminar/{id}", "/api/items/eliminar/{id}",
//										"/api/usuarios/usuarios/{id}").hasRole("ADMIN")

	}

	// Configuramos para que utilicen JWT.
	// Se encarga de guardar y generar el token con los datos del
	// accessTokenConverter.
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	/*
	 * Encargado de guardar los datos del usuario en el token, los claims. Además
	 * del user, los roles, tb puede guardar nombre apellido y email. Este
	 * componente se encarga de tomar esos valores y convertirlos en el token JWT
	 * codificados en Base 64.
	 */
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
		jwtAccessTokenConverter.setSigningKey(jwtKey);
		return jwtAccessTokenConverter;
	}

}
