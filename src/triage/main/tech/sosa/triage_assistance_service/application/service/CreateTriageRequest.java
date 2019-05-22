package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.application.dto.TriageDTO;

public class CreateTriageRequest {

    public TriageDTO triage;

    public CreateTriageRequest() {
    }

    public CreateTriageRequest(TriageDTO triage) {
        this.triage = triage;
    }
}
