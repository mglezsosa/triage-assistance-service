package tech.sosa.triage_assistance_service.application.service;

import tech.sosa.triage_assistance_service.application.dto.TriageDTO;

public class UpdateTriageRequest {

    public TriageDTO triageDTO;

    public UpdateTriageRequest(TriageDTO triageDTO) {
        this.triageDTO = triageDTO;
    }

    public TriageDTO triageDTO() {
        return triageDTO;
    }

    public void triageDTO(TriageDTO triageDTO) {
        this.triageDTO = triageDTO;
    }
}
