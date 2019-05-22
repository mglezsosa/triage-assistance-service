package tech.sosa.triage_assistance_service.infrastructure.springframework.boot;

import java.util.Objects;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;
import tech.sosa.triage_assistance_service.infrastructure.InMemoryTriageRepository;
import tech.sosa.triage_assistance_service.infrastructure.everit.json.schema.SCGExpressionValidator;
import tech.sosa.triage_assistance_service.infrastructure.springframework.boot.filter.JSONTriageValidationFilter;

@Configuration
public class Config {

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
    public FilterRegistrationBean<JSONTriageValidationFilter> jsonTriageValidationFilter() {
        FilterRegistrationBean<JSONTriageValidationFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new JSONTriageValidationFilter(triageJsonSchema()));
        registrationBean.addUrlPatterns("/triage/*");
        return registrationBean;
    }

    @Bean
    public TriageRepository inMemoryTriageRepository() {
        return new InMemoryTriageRepository();
    }

    @Bean
    public TriageMapper triageMapper() {
        return new TriageMapper();
    }
}
