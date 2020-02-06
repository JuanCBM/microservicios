package com.formacion.springboot.app.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

// @formatter:off
 
@Configuration
@EnableAuthorizationServer
// Clase de configuración con métodos para configurar el uso de JWT.
public class AurhorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	// Inyectamos los dos Beans que creamos en Spring SecurityConfig
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()");
	}

	// Configuramos las aplicaciones clientes (android, angular etc...) 
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// Tenemos una doble autenticación, una de los usuarios y otra de la aplicación frontend que utiliza nuestro backend.
		clients
			.inMemory() // Podriamos usar inMemory, Jdbc u otros almacenamientos.
			
			.withClient("frontendapp") // Mi cliente.
			.secret(bCryptPasswordEncoder.encode("12345")) // Contraseña del cliente encriptada.
			.scopes("read", "write") // Permisos de este cliente crear, modificar o eliminar.
			.authorizedGrantTypes("password", "refresh_token") // Refresh token se utiliza para obtener un nuevo token antes de que caduque el actual.
			/* Tipo de credenciales de los usuarios de la aplicación, cómo será la autenticación: Hay otros tipos
			 * -Usuarios: Intercambiamos las credenciales del usuario por el token
			 * -Authorization_code: Intercambio entre el código de autenticaciónque facilita el backend y el token.
			 * -Implícita: Se manda el client_id y el password y de respuesta se manda el token. (Poca seguridad)
			 */
			.accessTokenValiditySeconds(3600)// Segundos para caducar
			.refreshTokenValiditySeconds(3600)
			.and()
			.withClient("androidapp") 
			.secret(bCryptPasswordEncoder.encode("12345"))
			.scopes("read", "write")
			.authorizedGrantTypes("password", "refresh_token")
			.accessTokenValiditySeconds(3600)
			.refreshTokenValiditySeconds(3600);
			
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		/*
		 * Configurar el token storage y demás datos (claims) del token. Está
		 * relacionado con el endpoint de oauth2 que se encarga de generar el token POST
		 * a /oauth/token con param: user, pass, grand-type, client_id y generará token.
		 */
		endpoints.authenticationManager(authenticationManager)
			.tokenStore(tokenStore())
			.accessTokenConverter(accessTokenConverter());
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

//@formatter:on