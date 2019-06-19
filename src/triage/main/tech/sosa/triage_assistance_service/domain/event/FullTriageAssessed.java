package tech.sosa.triage_assistance_service.domain.event;

import tech.sosa.triage_assistance_service.util.event.Event;

public class FullTriageAssessed extends Event {

    private String chiefComplaintId;
    private String chiefComplaintTitle;
    private String title;
    private CriticalCheckTriageAssessed previousAssessment;

    public FullTriageAssessed(String chiefComplaintId, String chiefComplaintTitle,
            String title,
            CriticalCheckTriageAssessed previousAssessment) {
        this.chiefComplaintId = chiefComplaintId;
        this.chiefComplaintTitle = chiefComplaintTitle;
        this.title = title;
        this.previousAssessment = previousAssessment;
    }

    public String getChiefComplaintId() {
        return chiefComplaintId;
    }

    public String getChiefComplaintTitle() {
        return chiefComplaintTitle;
    }

    public String getTitle() {
        return title;
    }

    public CriticalCheckTriageAssessed getPreviousAssessment() {
        return previousAssessment;
    }
}
