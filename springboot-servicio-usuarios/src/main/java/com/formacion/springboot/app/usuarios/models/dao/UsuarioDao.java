package com.formacion.springboot.app.usuarios.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.formacion.springboot.app.commons.usuarios.models.entity.Usuario;

@RepositoryRestResource(path = "usuarios")
public interface UsuarioDao extends CrudRepository<Usuario, Long> {

	@RestResource(path = "buscar-username")
	public Usuario findByUsername(@Param("username") String username);

	// 'Usuario' es el nombre de la clase, y 'username' el nombre del atributo de la
	// entity, no de la tabla y columna de la BBDD.
	@Query("select u from Usuario u where u.username=?1")
	public Usuario obtenerPorUsername(String username);

	// Si queremos hacer la query nativa utilizamos la notación siguiente
//	@Query(value = "SELECT u FROM usuario u WHERE u.username=?1", nativeQuery = true)
//	public Usuario obtenerPorUsernameNativo(String username);

}
