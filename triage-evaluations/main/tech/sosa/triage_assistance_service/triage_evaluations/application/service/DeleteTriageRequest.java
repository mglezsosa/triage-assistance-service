package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import tech.sosa.triage_assistance_service.shared.application.service.ApplicationRequest;

public class DeleteTriageRequest implements ApplicationRequest {

    public final String chiefComplaintId;

    public DeleteTriageRequest(String chiefComplaintId) {
        this.chiefComplaintId = chiefComplaintId;
    }
}
