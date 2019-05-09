package tech.sosa.triage_assistance_service.domain.model;

import java.util.Collection;
import java.util.Objects;

public class Triage {

    private ChiefComplaint chiefComplaint;
    private TriageAlgorithm algorithm;

    public Triage(ChiefComplaint chiefComplaint,
            TriageAlgorithm algorithm) {
        this.chiefComplaint = chiefComplaint;
        this.algorithm = algorithm;
    }

    public TriageOutput assess(Collection<ClinicalFinding> findings) {
        return algorithm.evaluate(findings);
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
