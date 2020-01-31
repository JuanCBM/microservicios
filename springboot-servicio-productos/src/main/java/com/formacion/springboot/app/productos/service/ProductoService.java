package com.formacion.springboot.app.productos.service;

import java.util.List;

import com.formacion.springboot.app.commons.models.entity.Producto;

public interface ProductoService {
	public List<Producto> findAll();

	public Producto findById(Long id);

	public Producto save(Producto producto);

	public void deleteById(Long id);

	public Producto update(Producto producto, Long id);
}
