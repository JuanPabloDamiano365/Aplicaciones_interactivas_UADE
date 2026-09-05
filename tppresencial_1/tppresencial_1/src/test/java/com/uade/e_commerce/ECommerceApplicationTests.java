package com.uade.e_commerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test de "humo" (smoke test) generado por Spring Initializr.
 *
 * @SpringBootTest levanta todo el contexto de Spring (todos los beans:
 * Controllers, Services, Repositories) tal como lo haría la aplicación
 * real. Si algún bean está mal configurado (por ejemplo, una dependencia
 * que no se puede inyectar), este test falla y avisa el problema antes de
 * llegar a producción.
 */
@SpringBootTest
class ECommerceApplicationTests {

	// Test vacío a propósito: si el método se ejecuta sin lanzar una
	// excepción, significa que el contexto de Spring cargó correctamente.
	@Test
	void contextLoads() {
	}

}
