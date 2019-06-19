package tech.sosa.triage_assistance_service.application.service;

import java.util.Collection;
import tech.sosa.triage_assistance_service.application.ApplicationRequest;

public class FullyEvaluateRequest implements ApplicationRequest {

    public String previousPreTriageId;
    public String triageChiefComplaintId;
    public Collection<String> findingIds;

    public FullyEvaluateRequest(String triageChiefComplaintId, Collection<String> findingIds) {
        this.triageChiefComplaintId = triageChiefComplaintId;
        this.findingIds = findingIds;
    }

    public FullyEvaluateRequest(
            String triageChiefComplaintId,
            Collection<String> findingIds,
            String previousPreTriageId) {
        this(triageChiefComplaintId, findingIds);
        this.previousPreTriageId = previousPreTriageId;
    }

    public FullyEvaluateRequest() {
    }
}
