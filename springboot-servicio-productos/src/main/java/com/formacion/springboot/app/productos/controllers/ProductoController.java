package com.formacion.springboot.app.productos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.formacion.springboot.app.productos.models.entity.Producto;
import com.formacion.springboot.app.productos.models.service.ProductoService;

@RestController
@RequestMapping
public class ProductoController {

	protected static final String PRODUCTO_CONTROLLER = "/producto";
	protected static final String LISTAR_PRODUCTOS = "/listar";
	protected static final String VER_PRODUCTO = "/ver";
	protected static final String CREAR_PRODUCTO = "/crear";
	protected static final String EDITAR_PRODUCTO = "/editar";
	protected static final String ELIMINAR_PRODUCTO = "eliminar";

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
//			Thread.sleep(3000l);
//		} catch (final InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return producto;
	}

	@PostMapping(CREAR_PRODUCTO)
	@ResponseStatus(HttpStatus.CREATED)
	public Producto crear(@RequestBody Producto producto) {
		return productoService.save(producto);
	}

	@PutMapping(EDITAR_PRODUCTO + "/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto editar(@RequestBody Producto producto, @PathVariable Long id) {
		return productoService.update(producto, id);
	}

	@DeleteMapping(ELIMINAR_PRODUCTO + "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		productoService.deleteById(id);
	}

}
