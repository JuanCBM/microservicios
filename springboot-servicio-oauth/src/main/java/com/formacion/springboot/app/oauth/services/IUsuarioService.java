package com.formacion.springboot.app.oauth.services;

import com.formacion.springboot.app.commons.usuarios.models.entity.Usuario;

public interface IUsuarioService {
	public Usuario findByUserName(String username);
	
	public Usuario update(Usuario usuario, Long id);

}
