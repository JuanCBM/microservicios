package com.formacion.springboot.app.zuul.oauth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/security/oauth/token").permitAll()
								.antMatchers(HttpMethod.GET, "/api/productos/listar", "/api/items/listar", "/api/usuarios/usuarios").permitAll()
								.antMatchers(HttpMethod.GET, "/api/productos/ver/{id}", "/api/items/ver/{id}/cantidad/{cantidad}",
											"/api/usuarios/usuarios/{id}").hasAnyRole("ADMIN", "USER")
								.antMatchers("/api/productos/**", "/api/items/**","/api/usuarios/**").hasRole("ADMIN") // Sustituye a todo lo de debajo.

//								.antMatchers(HttpMethod.POST, "/api/productos/crear", "/api/items/crear").hasRole("ADMIN")
//								.antMatchers(HttpMethod.PUT, "/api/productos/editar/{id}", "/api/items/crear/{id}",
//											"/api/usuarios/usuarios/{id}").hasRole("ADMIN")
//								.antMatchers(HttpMethod.DELETE, "/api/productos/eliminar/{id}", "/api/items/eliminar/{id}",
//										"/api/usuarios/usuarios/{id}").hasRole("ADMIN")
								.anyRequest().authenticated()
								.and().cors().configurationSource(corsConfigurationSource())
								; 
		
		
	}		

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		// Debemos configurar los orígenes.
		corsConfiguration.addAllowedOrigin("*"); // Podrías poner "http:localhost..."
		// es posible usar setAllowedOrigin.
		corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
		corsConfiguration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTION"));
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration); // Ruta a mis microservicios
		return source;
		
	}
	
	// Registra el cors filter para que se aplique de forma global, no solo en spring security, en toda la aplicación.
	// Sólo se usa si la aplicación se registra en otro dominio.
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>
			(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
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
		jwtAccessTokenConverter.setSigningKey("algun_codigo_secreto_aeiou");
		return jwtAccessTokenConverter;
	}

}
