package co.edu.escuelaing.techcup.users;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "de.flapdoodle.mongodb.embedded.version=7.0.2")
@ActiveProfiles("test")
class ServiceUsersApplicationTests {

    @Test
    void contextLoads() {
    }
}
