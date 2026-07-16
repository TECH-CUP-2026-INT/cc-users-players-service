package co.edu.escuelaing.techcup.users.infrastructure.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.index.Index;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDbFactory,
                                       MappingMongoConverter mappingMongoConverter) {
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(mongoDbFactory, mappingMongoConverter);
    }

    @Bean
    public CommandLineRunner mongoIndexInitializer(MongoTemplate mongoTemplate) {
        return args -> {
            mongoTemplate.indexOps("usuarios").createIndex(new Index().on("correo", Sort.Direction.ASC).unique());
            mongoTemplate.indexOps("usuarios").createIndex(new Index().on("numeroIdentificacion", Sort.Direction.ASC).unique());
        };
    }
}
