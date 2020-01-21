package com.formacion.springboot.app.item.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.formacion.springboot.app.item.models.Item;
import com.formacion.springboot.app.item.models.Producto;
import com.formacion.springboot.app.item.models.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
@RequestMapping(value = "/item")
public class ItemController {

	protected static final String ITEM_CONTROLLER = "/item";
	protected static final String LISTAR_ITEM = "/listar";
	protected static final String VER_ITEM = "/ver";

	@Autowired
	@Qualifier("serviceRest")
	private ItemService itemService;

	@GetMapping(LISTAR_ITEM)
	public List<Item> listar() {
		return itemService.findAll();
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

}
