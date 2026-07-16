package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.repository;

import co.edu.escuelaing.techcup.users.core.domain.Usuario;
import co.edu.escuelaing.techcup.users.core.ports.out.UsuarioRepositoryPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepositoryPort {

    private final MongoTemplate mongoTemplate;

    public UsuarioRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Usuario save(Usuario usuario) {
        return mongoTemplate.save(usuario, "usuarios");
    }

    @Override
    public Optional<Usuario> findById(UUID id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Usuario.class, "usuarios"));
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        Query query = new Query(Criteria.where("correo").is(correo));
        return Optional.ofNullable(mongoTemplate.findOne(query, Usuario.class, "usuarios"));
    }

    @Override
    public Optional<Usuario> findByNumeroIdentificacion(String numeroIdentificacion) {
        Query query = new Query(Criteria.where("numeroIdentificacion").is(numeroIdentificacion));
        return Optional.ofNullable(mongoTemplate.findOne(query, Usuario.class, "usuarios"));
    }

    @Override
    public boolean existsByCorreo(String correo) {
        Query query = new Query(Criteria.where("correo").is(correo));
        return mongoTemplate.exists(query, "usuarios");
    }

    @Override
    public boolean existsByNumeroIdentificacion(String numeroIdentificacion) {
        Query query = new Query(Criteria.where("numeroIdentificacion").is(numeroIdentificacion));
        return mongoTemplate.exists(query, "usuarios");
    }

    @Override
    public void deleteById(UUID id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Usuario.class, "usuarios");
    }
}
