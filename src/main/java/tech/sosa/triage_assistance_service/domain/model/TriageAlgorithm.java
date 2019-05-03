package tech.sosa.triage_assistance_service.domain.model;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class TriageAlgorithm {

    private EmergencyLevel currentLevel;
    private Collection<ClinicalFinding> discriminators;
    private Collection<String> advices;
    private TriageAlgorithm lowerLevelAlgorithm;

    public TriageAlgorithm(EmergencyLevel currentLevel,
            Collection<ClinicalFinding> discriminators, Collection<String> advices,
            TriageAlgorithm lowerLevelAlgorithm) {
        this.currentLevel = currentLevel;
        this.discriminators = discriminators;
        this.advices = advices;
        this.lowerLevelAlgorithm = lowerLevelAlgorithm;
    }

    public EmergencyLevel assess(Collection<ClinicalFinding> clinicalFindings) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TriageAlgorithm algorithm = (TriageAlgorithm) o;
        return currentLevel.equals(algorithm.currentLevel) &&
                Objects.equals(discriminators, algorithm.discriminators) &&
                Objects.equals(advices, algorithm.advices) &&
                Objects.equals(lowerLevelAlgorithm, algorithm.lowerLevelAlgorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentLevel, discriminators, advices, lowerLevelAlgorithm);
    }

    @Override
    public String toString() {
        return "{ emergencyLevel: " + currentLevel.toString() + ", "
                + "discriminators: " + Optional.ofNullable(discriminators).map(Object::toString).orElse("null") + ", "
                + "advices: " + Optional.ofNullable(advices).map(Object::toString).orElse("null") + ", "
                + "default: " + Optional.ofNullable(lowerLevelAlgorithm).map(Object::toString).orElse("null") + " }";
    }
}
