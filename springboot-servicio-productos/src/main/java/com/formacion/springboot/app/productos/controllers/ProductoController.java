package com.formacion.springboot.app.productos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formacion.springboot.app.productos.models.entity.Producto;
import com.formacion.springboot.app.productos.models.service.ProductoService;

@RestController
@RequestMapping(value = "/producto")
public class ProductoController {

	protected static final String PRODUCTO_CONTROLLER = "/producto";
	protected static final String LISTAR_PRODUCTOS = "/listar";
	protected static final String VER_PRODUCTO = "/ver";

	@Autowired
	private Environment env;

	@Value("${server.port}")
	private Integer puerto;

	@Autowired
	private ProductoService productoService;

	@GetMapping(LISTAR_PRODUCTOS)
	public List<Producto> listar() {
		return productoService.findAll().stream().map(producto -> {
			// Integer.parseInt(env.getProperty("local.server.port"))
			producto.setPort(puerto);
			return producto;
		}).collect(Collectors.toList());
	}

	@GetMapping(VER_PRODUCTO + "/{id}")
	public Producto detalle(@PathVariable Long id) {
		final Producto producto = productoService.findById(id);
		// Integer.parseInt(env.getProperty("local.server.port"))
		producto.setPort(puerto);

//		final boolean ok = false;
//		if (ok == false) {
//			throw new Exception("No se pudo cargar el producto");
//		}

//		try {
//			Thread.sleep(5000l);
//		} catch (final InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return producto;
	}

}
