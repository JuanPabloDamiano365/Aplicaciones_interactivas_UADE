package com.uade.e_commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot.
 *
 * @SpringBootApplication es una anotación "combo" que agrupa:
 *   - @Configuration: la clase puede definir beans de configuración.
 *   - @EnableAutoConfiguration: Spring Boot configura automáticamente todo
 *     lo necesario (servidor web embebido, JPA, etc.) según las
 *     dependencias que están en el pom.xml.
 *   - @ComponentScan: Spring escanea el paquete actual (com.uade.e_commerce)
 *     y todos sus subpaquetes (model, dto, repository, service, controller,
 *     exception) en busca de clases anotadas con @Component, @Service,
 *     @Repository, @RestController, etc. para registrarlas como "beans".
 */
@SpringBootApplication
public class ECommerceApplication {

	// Punto de entrada de la aplicación: arranca el servidor embebido (Tomcat) en el puerto 8080
	public static void main(String[] args) {
		SpringApplication.run(ECommerceApplication.class, args);
	}

}
