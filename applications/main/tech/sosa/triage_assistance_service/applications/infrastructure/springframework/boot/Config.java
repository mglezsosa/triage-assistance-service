package tech.sosa.triage_assistance_service.applications.infrastructure.springframework.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.sosa.triage_assistance_service.applications.infrastructure.everit.json.schema.SCGExpressionValidator;
import tech.sosa.triage_assistance_service.applications.infrastructure.persistence.MongoDBTriageRepository;
import tech.sosa.triage_assistance_service.applications.port.adapter.DummyJWTAuthService;
import tech.sosa.triage_assistance_service.applications.port.adapter.MongoDBPendingTriagesQueue;
import tech.sosa.triage_assistance_service.identity_access.application.service.Authorize;
import tech.sosa.triage_assistance_service.identity_access.domain.model.AuthService;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;
import tech.sosa.triage_assistance_service.identity_access.port.adapter.HardcodedAuthService;

@Configuration
public class Config {

    @Bean
    public TriageMapper triageMapper() {
        return new TriageMapper();
    }

    @Bean
    public ObjectMapper jsonMapper() {
        return new ObjectMapper().registerModule(new Jdk8Module());
    }

    @Bean
    public SCGExpressionValidator scgExpressionValidator() {
        return new SCGExpressionValidator();
    }

    @Bean
    public Schema triageJsonSchema() {
        JSONObject rawSchema = new JSONObject(new JSONTokener(
                Objects.requireNonNull(
                        getClass().getClassLoader().getResourceAsStream("triage-json-schema.json"))
        ));

        SchemaLoader schemaLoader = SchemaLoader.builder()
                .schemaJson(rawSchema)
                .addFormatValidator(scgExpressionValidator())
                .build();

        return schemaLoader.load().build();
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://triage:secret@localhost:27017/?authSource=admin");
    }

    @Bean
    public TriageRepository mongoDBTriageRepository() {
        return new MongoDBTriageRepository(
                mongoClient()
                        .getDatabase("triage-management")
                        .getCollection("triages"),
                triageMapper(),
                jsonMapper()
        );
    }

    @Bean
    public PendingTriagesQueue pendingCasesQueue() {
        return new MongoDBPendingTriagesQueue(
                jsonMapper(),
                mongoClient()
                        .getDatabase("queue")
                        .getCollection("pending-triages")
        );
    }

    @Bean
    public AuthService authService() {
        return new HardcodedAuthService();
    }

    @Bean
    public Authorize authorizeUseCase() {
        return new Authorize(authService());
    }

    @Bean
    public tech.sosa.triage_assistance_service.applications.application.AuthService dummyJWTAuthService() {
        return new DummyJWTAuthService(authorizeUseCase());
    }

    @Bean
    public ConnectionFactory rabbitMQConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }

    @Bean
    public Connection rabbitMQConnection() throws IOException, TimeoutException {
        return rabbitMQConnectionFactory().newConnection();
    }

    @Bean
    public Channel rabbitMQChannel() throws IOException, TimeoutException {
        return rabbitMQConnection().createChannel();
    }

}
