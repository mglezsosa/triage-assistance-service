package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.application.ApplicationRequest;

public class ViewTriageRequest implements ApplicationRequest {

    public final String chiefComplaintId;

    public ViewTriageRequest(String chiefComplaintId) {
        this.chiefComplaintId = chiefComplaintId;
    }
}
