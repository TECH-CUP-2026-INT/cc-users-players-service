package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.repository;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.domain.enums.TipoIdentificacion;
import co.edu.escuelaing.techcup.users.core.domain.enums.UserType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica las consultas manuales de {@link MongoTemplate} (Criteria/Query)
 * contra un MongoDB real en contenedor, en vez de mockearlas.
 */
@Testcontainers(disabledWithoutDocker = true)
class UsuarioRepositoryImplTest {

    @Container
    private static final MongoDBContainer MONGO_CONTAINER =
            new MongoDBContainer(DockerImageName.parse("mongo:7.0"));

    private UsuarioRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        MongoTemplate mongoTemplate = new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(MONGO_CONTAINER.getConnectionString() + "/techcup_test?uuidRepresentation=standard"));
        repository = new UsuarioRepositoryImpl(mongoTemplate);
    }

    @AfterEach
    void limpiarColeccion() {
        new MongoTemplate(new SimpleMongoClientDatabaseFactory(
                MONGO_CONTAINER.getConnectionString() + "/techcup_test?uuidRepresentation=standard"))
                .getDb().getCollection("usuarios").drop();
    }

    private Usuario usuarioDePrueba(String correo, String numeroIdentificacion) {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Ada Lovelace");
        usuario.setCorreo(correo);
        usuario.setTipoIdentificacion(TipoIdentificacion.CC);
        usuario.setNumeroIdentificacion(numeroIdentificacion);
        usuario.setTipoUsuario(UserType.STUDENT);
        return usuario;
    }

    @Test
    void guardaYRecuperaPorId() {
        Usuario guardado = repository.save(usuarioDePrueba("ada@university.edu.co", "111"));

        Optional<Usuario> encontrado = repository.findById(guardado.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getCorreo()).isEqualTo("ada@university.edu.co");
    }

    @Test
    void findByIdRetornaEmptySiNoExiste() {
        assertThat(repository.findById(UUID.randomUUID())).isEmpty();
    }

    @Test
    void encuentraPorCorreo() {
        repository.save(usuarioDePrueba("ada@university.edu.co", "222"));

        Optional<Usuario> encontrado = repository.findByCorreo("ada@university.edu.co");

        assertThat(encontrado).isPresent();
    }

    @Test
    void encuentraPorNumeroDeIdentificacion() {
        repository.save(usuarioDePrueba("ada2@university.edu.co", "333"));

        Optional<Usuario> encontrado = repository.findByNumeroIdentificacion("333");

        assertThat(encontrado).isPresent();
    }

    @Test
    void existsByCorreoDistingueDuplicados() {
        repository.save(usuarioDePrueba("duplicado@university.edu.co", "444"));

        assertThat(repository.existsByCorreo("duplicado@university.edu.co")).isTrue();
        assertThat(repository.existsByCorreo("otro@university.edu.co")).isFalse();
    }

    @Test
    void existsByNumeroIdentificacionDistingueDuplicados() {
        repository.save(usuarioDePrueba("otra@university.edu.co", "555"));

        assertThat(repository.existsByNumeroIdentificacion("555")).isTrue();
        assertThat(repository.existsByNumeroIdentificacion("999")).isFalse();
    }

    @Test
    void deleteByIdEliminaElDocumento() {
        Usuario guardado = repository.save(usuarioDePrueba("borrar@university.edu.co", "666"));

        repository.deleteById(guardado.getId());

        assertThat(repository.findById(guardado.getId())).isEmpty();
    }
}
