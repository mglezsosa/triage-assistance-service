package tech.sosa.triage_assistance_service.domain.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class TriageOutput {

    private EmergencyLevel emergencyLevel;
    private Collection<String> advices;

    public TriageOutput(EmergencyLevel emergencyLevel, Collection<String> advices) {
        this.emergencyLevel = emergencyLevel;
        this.advices = Optional.ofNullable(advices).orElse(Collections.emptyList());
    }

    public EmergencyLevel emergencyLevel() {
        return emergencyLevel;
    }

    public Collection<String> advices() {
        return advices;
    }

    @Override
    public String toString() {
        return "{ emergencyLevel: " + emergencyLevel.toString() + ", "
                + "advices: " + advices.toString() + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TriageOutput that = (TriageOutput) o;
        return Objects.equals(emergencyLevel, that.emergencyLevel) &&
                Objects.equals(advices, that.advices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emergencyLevel, advices);
    }
}
