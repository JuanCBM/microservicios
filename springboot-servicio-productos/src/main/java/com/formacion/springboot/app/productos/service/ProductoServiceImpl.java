package com.formacion.springboot.app.productos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formacion.springboot.app.commons.models.entity.Producto;
import com.formacion.springboot.app.productos.models.dao.ProductoDao;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoDao productoDao;

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAll() {
		return (List<Producto>) productoDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findById(Long id) {
		return productoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Producto save(Producto producto) {
		return productoDao.save(producto);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		productoDao.deleteById(id);

	}

	@Override
	public Producto update(Producto producto, Long id) {
		final Producto productoDb = findById(id);
		productoDb.setNombre(producto.getNombre());
		productoDb.setPrecio(producto.getPrecio());

		return productoDao.save(productoDb);
	}

}
