package tech.sosa.triage_assistance_service.triage_evaluations.domain.model;

import java.util.Objects;

public class AlgorithmLevelTitle {

    private String level;

    public AlgorithmLevelTitle(String level) {
        this.level = level;
    }

    public String value() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AlgorithmLevelTitle that = (AlgorithmLevelTitle) o;
        return level.equals(that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(level);
    }

    @Override
    public String toString() {
        return level;
    }
}
