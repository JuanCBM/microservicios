package com.formacion.springboot.app.item.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.formacion.springboot.app.item.models.Producto;

// Sigue estando acoplado al usar Feign, (name = "servicio-productos", url = "localhost:8001")
@FeignClient(name = "servicio-productos")
public interface ProductoClienteRest {

	@GetMapping("/producto/listar")
	public List<Producto> listar();

	@GetMapping("/producto/ver" + "/{id}")
	public Producto detalle(@PathVariable Long id);

}
