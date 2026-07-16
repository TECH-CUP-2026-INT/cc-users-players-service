package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.repository;

import co.edu.escuelaing.techcup.users.core.domain.OTP;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica las consultas manuales de {@link MongoTemplate} para OTP
 * (filtro por usado/expiración y orden por fecha de creación) contra un
 * MongoDB real en contenedor.
 */
@Testcontainers(disabledWithoutDocker = true)
class OTPRepositoryImplTest {

    @Container
    private static final MongoDBContainer MONGO_CONTAINER =
            new MongoDBContainer(DockerImageName.parse("mongo:7.0"));

    private OTPRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MongoTemplate mongoTemplate = new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(MONGO_CONTAINER.getConnectionString() + "/techcup_test"));
        repository = new OTPRepositoryImpl(mongoTemplate);
    }

    @AfterEach
    void limpiarColeccion() {
        new MongoTemplate(new SimpleMongoClientDatabaseFactory(
                MONGO_CONTAINER.getConnectionString() + "/techcup_test"))
                .getDb().getCollection("otps").drop();
    }

    private OTP otpDePrueba(UUID usuarioId, boolean usado, LocalDateTime fechaExpiracion) {
        OTP otp = new OTP();
        otp.setUsuarioId(usuarioId);
        otp.setCodigoOTP("123456");
        otp.setUsado(usado);
        otp.setFechaExpiracion(fechaExpiracion);
        return otp;
    }

    @Test
    void encuentraElOtpVigenteNoUsado() {
        UUID usuarioId = UUID.randomUUID();
        repository.save(otpDePrueba(usuarioId, false, LocalDateTime.now().plusMinutes(15)));

        Optional<OTP> encontrado = repository
                .findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(usuarioId, LocalDateTime.now());

        assertThat(encontrado).isPresent();
    }

    @Test
    void ignoraOtpsYaUsados() {
        UUID usuarioId = UUID.randomUUID();
        repository.save(otpDePrueba(usuarioId, true, LocalDateTime.now().plusMinutes(15)));

        Optional<OTP> encontrado = repository
                .findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(usuarioId, LocalDateTime.now());

        assertThat(encontrado).isEmpty();
    }

    @Test
    void ignoraOtpsExpirados() {
        UUID usuarioId = UUID.randomUUID();
        repository.save(otpDePrueba(usuarioId, false, LocalDateTime.now().minusMinutes(1)));

        Optional<OTP> encontrado = repository
                .findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(usuarioId, LocalDateTime.now());

        assertThat(encontrado).isEmpty();
    }

    @Test
    void retornaElMasRecienteCuandoHayVariosVigentes() throws InterruptedException {
        UUID usuarioId = UUID.randomUUID();
        OTP primero = otpDePrueba(usuarioId, false, LocalDateTime.now().plusMinutes(15));
        primero.setCodigoOTP("111111");
        repository.save(primero);
        Thread.sleep(10);
        OTP segundo = otpDePrueba(usuarioId, false, LocalDateTime.now().plusMinutes(15));
        segundo.setCodigoOTP("222222");
        repository.save(segundo);

        Optional<OTP> encontrado = repository
                .findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(usuarioId, LocalDateTime.now());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCodigoOTP()).isEqualTo("222222");
    }
}
