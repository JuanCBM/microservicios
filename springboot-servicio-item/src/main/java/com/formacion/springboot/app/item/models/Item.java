package com.formacion.springboot.app.item.models;

import com.formacion.springboot.app.commons.models.entity.Producto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	private Producto producto;
	private Integer cantidad;

	public Double getTotal() {
		return producto.getPrecio() * cantidad.doubleValue();
	}

}
