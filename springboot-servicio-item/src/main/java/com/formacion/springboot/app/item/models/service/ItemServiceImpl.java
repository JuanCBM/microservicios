package com.formacion.springboot.app.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.formacion.springboot.app.item.models.Item;
import com.formacion.springboot.app.item.models.Producto;

@Service("serviceRest")
public class ItemServiceImpl implements ItemService {

	@Autowired
	public RestTemplate clienteRest;

	@Override
	public List<Item> findAll() {
		// http://localhost:8001/producto/listar
		// En lugar de especificar la instancia, ponemos el microservicio al que
		// queremos conectarnos.
		final List<Producto> productos = Arrays
				.asList(clienteRest.getForObject("http://servicio-productos/listar", Producto[].class));
		return productos.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		final Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());

		final Producto producto = clienteRest.getForObject("http://servicio-productos/ver/{id}", Producto.class,
				pathVariables);
		return new Item(producto, cantidad);
	}

	@Override
	public Producto save(Producto producto) {
		final HttpEntity<Producto> body = new HttpEntity<Producto>(producto);

		final ResponseEntity<Producto> response = clienteRest.exchange("http://servicio-productos/crear",
				HttpMethod.POST, body, Producto.class);

		final Producto productoResponse = response.getBody();

		return productoResponse;
	}

	@Override
	public Producto update(Producto producto, Long id) {
		final Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());

		final HttpEntity<Producto> body = new HttpEntity<Producto>(producto);

		final ResponseEntity<Producto> response = clienteRest.exchange("http://servicio-productos/editar/{id}",
				HttpMethod.PUT, body, Producto.class, pathVariables);

		final Producto productoResponse = response.getBody();

		return productoResponse;

	}

	@Override
	public void delete(Long id) {
		final Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());

		clienteRest.delete("http://servicio-productos/eliminar/{id}", pathVariables);
	}

}
