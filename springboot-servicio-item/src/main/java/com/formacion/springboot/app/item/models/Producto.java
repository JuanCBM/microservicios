package com.formacion.springboot.app.item.models;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Producto {
	private Long id;
	private String nombre;
	private Double precio;
	private Date createAt;
	private Integer port;
}
