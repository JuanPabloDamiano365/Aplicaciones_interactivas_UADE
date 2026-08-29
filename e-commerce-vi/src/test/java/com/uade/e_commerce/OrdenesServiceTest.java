package com.uade.e_commerce;

import static org.assertj.core.api.Assertions.assertThat;

import com.uade.e_commerce.model.OrdenItem;
import com.uade.e_commerce.model.Ordenes;
import com.uade.e_commerce.model.Producto;
import com.uade.e_commerce.dto.PedidoRequest;
import com.uade.e_commerce.dto.PedidoItemRequest;
import com.uade.e_commerce.repository.ProductoRepository;
import com.uade.e_commerce.service.OrdenesService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:ordenes-test",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class OrdenesServiceTest {

    @Autowired
    private OrdenesService ordenesService;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void crearPedidoCalculaTotalYDescuentaStock() {
        Producto producto = productoRepository.save(new Producto(
                null,
                "Street Runner",
                "UADE Shoes",
                "UNISEX",
                42,
                "Zapatilla urbana",
                "https://example.com/street-runner.jpg",
                50000.0,
                5,
                "URBANO",
                true));

        PedidoRequest request = new PedidoRequest("lucia.gomez@example.com",
            List.of(new PedidoItemRequest(producto.getId(), 2)));

        Ordenes creado = ordenesService.crearOrden(request);

        assertThat(creado.getId()).isNotNull();
        assertThat(creado.getEstado()).isEqualTo("PENDIENTE");
        assertThat(creado.getTotal()).isEqualTo(100000.0);
        assertThat(productoRepository.findById(producto.getId()).orElseThrow().getStock()).isEqualTo(3);
    }
}
