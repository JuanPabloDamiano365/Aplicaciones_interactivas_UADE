package com.uade.e_commerce;

import static org.assertj.core.api.Assertions.assertThat;

import com.uade.e_commerce.model.Usuario;
import com.uade.e_commerce.dto.UsuarioRegistroRequest;
import com.uade.e_commerce.repository.UsuarioRepository;
import com.uade.e_commerce.service.UsuarioService;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:usuarios-test",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registraUsuarioYLoEncuentraEnLaBase() {
        UsuarioRegistroRequest request = new UsuarioRegistroRequest(
            "Lucia", "Gomez", "lucia.gomez@example.com", "password-segura",
            LocalDate.of(2000, 5, 20), "FEMENINO");

        Usuario guardado = usuarioService.registrar(request);
        usuarioRepository.flush();

        assertThat(guardado.getId()).isNotNull();
        assertThat(passwordEncoder.matches("password-segura", guardado.getPassword())).isTrue();
        Usuario encontrado = usuarioRepository.findByEmailIgnoreCase("LUCIA.GOMEZ@EXAMPLE.COM").orElseThrow();
        assertThat(encontrado.getFechaNacimiento()).isEqualTo(LocalDate.of(2000, 5, 20));
        assertThat(encontrado.getSexo()).isEqualTo("FEMENINO");
    }
}
