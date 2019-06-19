package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.application.ApplicationRequest;

public class DeleteTriageRequest implements ApplicationRequest {

    public final String chiefComplaintId;

    public DeleteTriageRequest(String chiefComplaintId) {
        this.chiefComplaintId = chiefComplaintId;
    }
}
