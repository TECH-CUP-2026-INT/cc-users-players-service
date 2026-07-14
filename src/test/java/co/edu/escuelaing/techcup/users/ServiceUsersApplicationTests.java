package co.edu.escuelaing.techcup.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")  // ← agregar esta línea
class ServiceUsersApplicationTests {
    @Test
    void contextLoads() {}
}


