package com.formacion.springboot.app.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.formacion.springboot.app.item.models.Item;
import com.formacion.springboot.app.item.models.Producto;

@Service("serviceRest")
public class ItemServiceImpl implements ItemService {

	@Autowired
	private RestTemplate clienteRest;

	@Override
	public List<Item> findAll() {
		// http://localhost:8001/producto/listar
		// En lugar de especificar la instancia, ponemos el microservicio al que
		// queremos conectarnos.
		final List<Producto> productos = Arrays
				.asList(clienteRest.getForObject("http://servicio-productos/producto/listar", Producto[].class));
		return productos.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		final Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());

		final Producto producto = clienteRest.getForObject("http://servicio-productos/producto/ver/{id}",
				Producto.class, pathVariables);
		return new Item(producto, cantidad);
	}

}
