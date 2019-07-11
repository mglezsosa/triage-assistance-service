package tech.sosa.triage_assistance_service.triage_evaluations.domain.model;

import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;

public class StatefulCriticalCheckTriageAssessed extends CriticalCheckTriageAssessed {

    public enum Status {
        PENDING,
        IN_PROCESS
    }

    private Status status;

    public StatefulCriticalCheckTriageAssessed(
            String id,
            ChiefComplaint foundChiefComplaint,
            CriticalCheckAssesmentOutput output,
            Status status) {
        super(id, foundChiefComplaint, output);
        this.status = status;
    }

    public static StatefulCriticalCheckTriageAssessed create(
            CriticalCheckTriageAssessed assessing
    ) {
        return new StatefulCriticalCheckTriageAssessed(
                assessing.getId(),
                assessing.foundChiefComplaint(),
                assessing.getOutput(),
                Status.PENDING);
    }

    public Status getStatus() {
        return status;
    }

    public void startProcessing() {
        this.status = Status.IN_PROCESS;
    }
}