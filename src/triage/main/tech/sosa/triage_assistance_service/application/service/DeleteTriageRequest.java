package tech.sosa.triage_assistance_service.application.service;

public class DeleteTriageRequest {

    public final String chiefComplaintId;

    public DeleteTriageRequest(String chiefComplaintId) {
        this.chiefComplaintId = chiefComplaintId;
    }
}
