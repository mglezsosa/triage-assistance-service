package tech.sosa.triage_assistance_service.application.service;

import java.util.Collection;
import tech.sosa.triage_assistance_service.application.ApplicationRequest;

public class CheckForCriticalStateRequest implements ApplicationRequest {

    public String triageChiefComplaintId;
    public Collection<String> findingIds;

    public CheckForCriticalStateRequest(String triageChiefComplaintId, Collection<String> findingIds) {
        this.triageChiefComplaintId = triageChiefComplaintId;
        this.findingIds = findingIds;
    }

    public CheckForCriticalStateRequest() {
    }
}
