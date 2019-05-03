package tech.sosa.triage_assistance_service.domain.model;

import java.util.Objects;

public class EmergencyLevel {

    private String level;

    public EmergencyLevel(String level) {
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
        EmergencyLevel that = (EmergencyLevel) o;
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
