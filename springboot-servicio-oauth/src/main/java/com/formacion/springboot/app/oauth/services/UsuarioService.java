package com.formacion.springboot.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.formacion.springboot.app.commons.usuarios.models.entity.Usuario;
import com.formacion.springboot.app.oauth.clients.UsuarioFeignClient;

@Service
public class UsuarioService implements UserDetailsService, IUsuarioService {

	private Logger log = LoggerFactory.getLogger(UserDetailsService.class);
	@Autowired
	UsuarioFeignClient usuarioFeignClient;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioFeignClient.findByUsername(username);

		if (usuario == null) {
			log.error("Error en el login, el usuario '" + username + "'no existe");
			throw new UsernameNotFoundException("Error en el login, el usuario '" + username + "'no existe");
		}
		List<GrantedAuthority> authorities = usuario.getRoles().stream()
				.map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
				.peek(authority -> log.info("Rol: " + authority.getAuthority())).collect(Collectors.toList());

		log.info("Usuario autenticado: " + username);
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true,
				authorities);
	}

	@Override
	public Usuario findByUserName(String username) {

		return usuarioFeignClient.findByUsername(username);
	}

}
