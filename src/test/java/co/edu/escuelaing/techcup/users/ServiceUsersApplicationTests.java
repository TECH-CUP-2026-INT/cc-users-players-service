package co.edu.escuelaing.techcup.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Verifica que el contexto de Spring Boot levanta correctamente.
 * Flapdoodle (MongoDB embebido) se activa automáticamente en tests
 * sin necesidad de un perfil separado.
 */
@SpringBootTest
@ActiveProfiles("test")
class ServiceUsersApplicationTests {

    @Test
    void contextLoads() {
    }
}