package tech.sosa.triage_assistance_service.triage_evaluations.domain.event;

import java.util.Objects;
import java.util.UUID;

import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.CriticalCheckAssesmentOutput;
import tech.sosa.triage_assistance_service.shared.domain.event.Event;

public class CriticalCheckTriageAssessed extends Event {

    private final String id;
    private final ChiefComplaint foundChiefComplaint;
    private final CriticalCheckAssesmentOutput output;

    public CriticalCheckTriageAssessed(String id,
            ChiefComplaint foundChiefComplaint,
            CriticalCheckAssesmentOutput output) {
        this.id = id;
        this.foundChiefComplaint = foundChiefComplaint;
        this.output = output;
    }

    public static CriticalCheckTriageAssessed create(
            ChiefComplaint foundChiefComplaint,
            CriticalCheckAssesmentOutput output
    ) {
        return new CriticalCheckTriageAssessed(
                UUID.randomUUID().toString(),
                foundChiefComplaint,
                output
        );
    }

    public ChiefComplaint foundChiefComplaint() {
        return foundChiefComplaint;
    }

    public String getId() {
        return id;
    }

    public String getFoundChiefComplaintId() {
        return foundChiefComplaint.id().value();
    }

    public String getFoundChiefComplaintTitle() {
        return foundChiefComplaint.title().value();
    }

    public CriticalCheckAssesmentOutput getOutput() {
        return output;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CriticalCheckTriageAssessed that = (CriticalCheckTriageAssessed) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(foundChiefComplaint, that.foundChiefComplaint) &&
                Objects.equals(output, that.output);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foundChiefComplaint, output);
    }
}
