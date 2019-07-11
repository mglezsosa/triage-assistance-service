package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import tech.sosa.triage_assistance_service.shared.application.service.ApplicationRequest;

public class ViewTriageRequest implements ApplicationRequest {

    public final String chiefComplaintId;

    public ViewTriageRequest(String chiefComplaintId) {
        this.chiefComplaintId = chiefComplaintId;
    }
}
