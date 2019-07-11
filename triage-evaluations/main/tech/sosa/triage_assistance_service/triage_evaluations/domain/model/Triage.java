package tech.sosa.triage_assistance_service.triage_evaluations.domain.model;

import java.util.Collection;
import java.util.Objects;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.FullTriageAssessed;
import tech.sosa.triage_assistance_service.shared.domain.event.EventPublisher;

public class Triage {

    private ChiefComplaint chiefComplaint;
    private TriageAlgorithm algorithm;

    public Triage(ChiefComplaint chiefComplaint,
            TriageAlgorithm algorithm) {
        this.chiefComplaint = chiefComplaint;
        this.algorithm = algorithm;
    }

    public AlgorithmLevel fullyAssess(Collection<ClinicalFinding> findings) {
        return fullyAssess(findings, null);
    }

    public AlgorithmLevel fullyAssess(
            Collection<ClinicalFinding> findings,
            CriticalCheckTriageAssessed previousAssessment) {
        AlgorithmLevel level = algorithm.evaluate(findings);

        EventPublisher.instance().publish(new FullTriageAssessed(
                this.chiefComplaint.id().value(),
                this.chiefComplaint.title().value(),
                level.title().value(),
                previousAssessment
        ));

        return level;
    }

    public CriticalCheckAssesmentOutput checkForCriticalState(Collection<ClinicalFinding> findings) {
        CriticalCheckAssesmentOutput output = CriticalCheckAssesmentOutput.fromAlgorithmLevel(
                algorithm.criticalStateSubalgorithm().evaluate(findings)
        );

        EventPublisher.instance().publish(CriticalCheckTriageAssessed.create(chiefComplaint, output));

        return output;
    }

    public ChiefComplaint chiefComplaint() {
        return chiefComplaint;
    }

    public TriageAlgorithm algorithm() {
        return algorithm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Triage triage = (Triage) o;
        return chiefComplaint.equals(triage.chiefComplaint) &&
                algorithm.equals(triage.algorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chiefComplaint, algorithm);
    }

    @Override
    public String toString() {
        return "{ chiefComplaint: " + chiefComplaint.toString() + ", "
                + "algorithm: " + algorithm.toString() + " }";
    }
}
