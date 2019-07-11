package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import tech.sosa.triage_assistance_service.shared.application.service.ApplicationRequest;
import tech.sosa.triage_assistance_service.shared.application.dto.TriageDTO;

public class CreateTriageRequest implements ApplicationRequest {

    public TriageDTO triage;

    public CreateTriageRequest() {
    }

    public CreateTriageRequest(TriageDTO triage) {
        this.triage = triage;
    }
}
