package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.application.ApplicationRequest;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;

public class UpdateTriageRequest implements ApplicationRequest {

    public TriageDTO triage;

    public UpdateTriageRequest() {
    }

    public UpdateTriageRequest(TriageDTO triage) {
        this.triage = triage;
    }
}
