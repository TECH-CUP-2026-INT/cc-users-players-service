package co.edu.escuelaing.techcup.users.infrastructure.adapter.out.repository;

import co.edu.escuelaing.techcup.users.core.domain.OTP;
import co.edu.escuelaing.techcup.users.core.ports.out.OTPRepositoryPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OTPRepositoryImpl implements OTPRepositoryPort {

    private final MongoTemplate mongoTemplate;

    public OTPRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public OTP save(OTP otp) {
        return mongoTemplate.save(otp, "otps");
    }

    @Override
    public Optional<OTP> findTopByUsuarioIdAndUsadoFalseAndFechaExpiracionAfter(UUID usuarioId, LocalDateTime now) {
        Query query = new Query(
            Criteria.where("usuarioId").is(usuarioId)
                .and("usado").is(false)
                .and("fechaExpiracion").gt(now)
        );
        query.limit(1);
        query.with(org.springframework.data.domain.Sort.by(
            org.springframework.data.domain.Sort.Direction.DESC, "fechaCreacion"
        ));
        return Optional.ofNullable(mongoTemplate.findOne(query, OTP.class, "otps"));
    }
}
