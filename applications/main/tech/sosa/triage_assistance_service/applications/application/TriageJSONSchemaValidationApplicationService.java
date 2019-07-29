package tech.sosa.triage_assistance_service.applications.application;

import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.json.JSONTokener;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationRequest;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;

public class TriageJSONSchemaValidationApplicationService<S, T extends ApplicationRequest>
        implements ApplicationService<S, T> {

    private ApplicationService<S, T> proxiedApplicationService;
    private Schema jsonSchema;
    private String requestBody;

    public TriageJSONSchemaValidationApplicationService(
            ApplicationService<S, T> proxiedApplicationService,
            Schema jsonSchema,
            String requestBody) {
        this.proxiedApplicationService = proxiedApplicationService;
        this.jsonSchema = jsonSchema;
        this.requestBody = requestBody;
    }

    @Override
    public S execute(T request) {
        var json = new JSONObject(
                new JSONTokener(requestBody)
        );
        jsonSchema.validate(json.getJSONObject("triage"));
        return proxiedApplicationService.execute(request);
    }

}
