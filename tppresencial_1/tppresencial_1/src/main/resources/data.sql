-- =====================================================================
-- data.sql
-- ========
-- Spring Boot ejecuta este script automáticamente al arrancar (una vez
-- que Hibernate ya creó las tablas, gracias a
-- "spring.jpa.defer-datasource-initialization=true" en
-- application.properties). Sirve para precargar datos de ejemplo y poder
-- probar los endpoints de la API inmediatamente, sin tener que cargar
-- categorías y productos a mano.
--
-- Nota: como "spring.jpa.hibernate.ddl-auto=update" no borra datos
-- existentes, estos INSERT se protegen con "WHERE NOT EXISTS" para no
-- duplicar filas cada vez que se reinicia la aplicación.
-- =====================================================================

-- Categorías de ejemplo
INSERT INTO categoria (nombre)
SELECT * FROM (SELECT 'Running') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Running');

INSERT INTO categoria (nombre)
SELECT * FROM (SELECT 'Urbanas') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Urbanas');

INSERT INTO categoria (nombre)
SELECT * FROM (SELECT 'Basketball') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Basketball');

-- Productos de ejemplo, cada uno asociado a una categoría existente mediante subconsulta
INSERT INTO productos (nombre, genero, marca, descripcion, precio, color, stock, imagen_url, categoria_id)
SELECT 'Air Runner X1', 'Unisex', 'Nike', 'Zapatilla de running liviana, ideal para largas distancias', 89999.0, 'Negro', 25, NULL,
       (SELECT id FROM categoria WHERE nombre = 'Running')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Air Runner X1');

INSERT INTO productos (nombre, genero, marca, descripcion, precio, color, stock, imagen_url, categoria_id)
SELECT 'Street Classic', 'Hombre', 'Adidas', 'Zapatilla urbana de estilo retro', 74999.0, 'Blanco', 40, NULL,
       (SELECT id FROM categoria WHERE nombre = 'Urbanas')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Street Classic');

INSERT INTO productos (nombre, genero, marca, descripcion, precio, color, stock, imagen_url, categoria_id)
SELECT 'Jump Pro 23', 'Hombre', 'Puma', 'Zapatilla de basketball con amortiguación reforzada', 109999.0, 'Rojo', 15, NULL,
       (SELECT id FROM categoria WHERE nombre = 'Basketball')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'Jump Pro 23');

INSERT INTO productos (nombre, genero, marca, descripcion, precio, color, stock, imagen_url, categoria_id)
SELECT 'City Walk W', 'Mujer', 'Nike', 'Zapatilla urbana cómoda para uso diario', 69999.0, 'Rosa', 30, NULL,
       (SELECT id FROM categoria WHERE nombre = 'Urbanas')
WHERE NOT EXISTS (SELECT 1 FROM productos WHERE nombre = 'City Walk W');

-- Usuarios de ejemplo para probar los tres roles desde el inicio del programa
INSERT INTO usuarios (nombre, email, password, rol, direccion, telefono)
SELECT 'Usuario Demo', 'demo@tppresencial.com', '1234', 'USUARIO', 'Av. Siempre Viva 742', '11-5555-5555'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'demo@tppresencial.com');

INSERT INTO usuarios (nombre, email, password, rol, direccion, telefono)
SELECT 'Vendedor Demo', 'vendedor@tppresencial.com', '1234', 'VENDEDOR', 'San Martín 456', '11-6666-6666'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'vendedor@tppresencial.com');

INSERT INTO usuarios (nombre, email, password, rol, direccion, telefono)
SELECT 'Admin Demo', 'admin@tppresencial.com', '1234', 'ADMIN', 'Corrientes 789', '11-7777-7777'
WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@tppresencial.com');

-- Carrito vacío asociado al usuario de ejemplo (relación 1 a 1 Usuario-Carrito)
INSERT INTO carrito (usuario_id, fecha_creacion)
SELECT (SELECT id FROM usuarios WHERE email = 'demo@tppresencial.com'), CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM carrito WHERE usuario_id = (SELECT id FROM usuarios WHERE email = 'demo@tppresencial.com'));

-- Un carrito adicional para el vendedor de ejemplo si se quiere probar flujo vendedor
INSERT INTO carrito (usuario_id, fecha_creacion)
SELECT (SELECT id FROM usuarios WHERE email = 'vendedor@tppresencial.com'), CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM carrito WHERE usuario_id = (SELECT id FROM usuarios WHERE email = 'vendedor@tppresencial.com'));
