package tech.sosa.triage_assistance_service.applications.springframework.boot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.Objects;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.sosa.triage_assistance_service.applications.everit.json.schema.SCGExpressionValidator;
import tech.sosa.triage_assistance_service.applications.persistence.MongoDBTriageRepository;
import tech.sosa.triage_assistance_service.applications.port.adapter.MongoDBPendingTriagesQueue;
import tech.sosa.triage_assistance_service.applications.springframework.boot.filter.EventPublisherResetFilter;
import tech.sosa.triage_assistance_service.applications.springframework.boot.filter.JSONTriageValidationFilter;
import tech.sosa.triage_assistance_service.identity_access.application.service.Authorize;
import tech.sosa.triage_assistance_service.identity_access.domain.model.AuthService;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;
import tech.sosa.triage_assistance_service.identity_access.port.adapter.HardcodedAuthService;
import tech.sosa.triage_assistance_service.identity_access.port.adapter.LoggingEventStore;

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
    public PendingTriagesQueue inMemoryQueue() {
        return new MongoDBPendingTriagesQueue(
                jsonMapper(),
                mongoClient()
                        .getDatabase("queue")
                        .getCollection("pending-triages")
        );
    }

    @Bean
    public FilterRegistrationBean<EventPublisherResetFilter> eventPublisherResetFilterFilter() {
        FilterRegistrationBean<EventPublisherResetFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new EventPublisherResetFilter(
                new LoggingEventStore(
                        jsonMapper()
                ),
                inMemoryQueue()
        ));

        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JSONTriageValidationFilter> jsonTriageValidationFilter() {
        FilterRegistrationBean<JSONTriageValidationFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new JSONTriageValidationFilter(triageJsonSchema()));
        registrationBean.addUrlPatterns("/triage/*");
        return registrationBean;
    }

    @Bean
    public AuthService authService() {
        return new HardcodedAuthService();
    }

    @Bean
    public Authorize authorizeUseCase() {
        return new Authorize(authService());
    }

//    @Bean
//    public TriageRepository inMemoryTriageRepository() {
//        return new InMemoryTriageRepository();
//    }

}
