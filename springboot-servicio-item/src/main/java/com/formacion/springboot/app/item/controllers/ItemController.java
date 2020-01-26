package com.formacion.springboot.app.item.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.formacion.springboot.app.item.models.Item;
import com.formacion.springboot.app.item.models.Producto;
import com.formacion.springboot.app.item.models.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping
@RefreshScope
public class ItemController {

	private static Logger log = LoggerFactory.getLogger(ItemController.class);
	@Autowired
	private Environment env;

	protected static final String ITEM_CONTROLLER = "/item";
	protected static final String LISTAR_ITEM = "/listar";
	protected static final String VER_ITEM = "/ver";
	protected static final String CONFIG = "/obtener-config";
	protected static final String CREAR_PRODUCTO = "/crear";
	protected static final String EDITAR_PRODUCTO = "/editar";
	protected static final String ELIMINAR_PRODUCTO = "eliminar";

	@Value("${configuracion.texto}")
	private String texto;

	@Autowired
	@Qualifier("serviceRest") // serviceFeign || serviceRest
	private ItemService itemService;

	@GetMapping(LISTAR_ITEM)
	public List<Item> listar() {
		return itemService.findAll();
	}

	@GetMapping(CONFIG)
	public ResponseEntity<?> config(@Value("${server.port}") String puerto) {
		log.info(texto);

		final Map<String, String> json = new HashMap<>();
		json.put("texto", texto);
		json.put("puerto", puerto);

		if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("autor.email", env.getProperty("configuracion.autor.email"));

		}
		if (env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("prod")) {

		}

		return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
	}

	@HystrixCommand(fallbackMethod = "metodoAlternativo")
	@GetMapping(VER_ITEM + "/{id}" + "/{cantidad}")
	public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);
	}

	public Item metodoAlternativo(Long id, Integer cantidad) {
		final Item item = new Item();
		final Producto producto = new Producto();
		producto.setNombre("Nombre Preteterminado");
		producto.setPrecio(500.00);
		item.setProducto(producto);
		item.setCantidad(cantidad);

		return item;

	}

	@PostMapping(CREAR_PRODUCTO)
	@ResponseStatus(HttpStatus.CREATED)
	public Producto crear(@RequestBody Producto producto) {
		return itemService.save(producto);
	}

	@PutMapping(EDITAR_PRODUCTO + "/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto editar(@RequestBody Producto producto, @PathVariable Long id) {
		return itemService.update(producto, id);
	}

	@DeleteMapping(ELIMINAR_PRODUCTO + "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		itemService.delete(id);
	}

}
