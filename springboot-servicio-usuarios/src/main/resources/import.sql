INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES ('andres','$2a$10$sxaCjbRxnL6FuRTJxsfh7uRZHoPedz2Eys9BnlvIn3fOwY1gKi7XW',1, 'Andres', 'Guzman','profesor@bolsadeideas.com');
INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES ('admin','$2a$10$jqc6jawkAXfvA3mFeHemdOQnFjEuJJQzKQ/5DnhSGuH7oNxQvNiuW',1, 'John', 'Doe','jhon.doe@bolsadeideas.com');

INSERT INTO `roles` (nombre) VALUES ('ROL_USER');
INSERT INTO `roles` (nombre) VALUES ('ROL_ADMIN');

INSERT INTO `usuarios_roles` (usuario_id, rol_id) VALUES (1, 1);
INSERT INTO `usuarios_roles` (usuario_id, rol_id) VALUES (2, 2);
INSERT INTO `usuarios_roles` (usuario_id, rol_id) VALUES (2, 1);
