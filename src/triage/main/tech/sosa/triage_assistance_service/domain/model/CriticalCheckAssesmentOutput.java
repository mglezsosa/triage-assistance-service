package tech.sosa.triage_assistance_service.domain.model;

import java.util.Collection;
import java.util.Objects;

public class CriticalCheckAssesmentOutput {

    public boolean hasCriticalState;
    public Collection<String> advices;

    public CriticalCheckAssesmentOutput(boolean hasCriticalState,
            Collection<String> advices) {
        this.hasCriticalState = hasCriticalState;
        this.advices = advices;
    }

    public static CriticalCheckAssesmentOutput fromAlgorithmLevel(AlgorithmLevel algorithmLevel) {
        return new CriticalCheckAssesmentOutput(
                algorithmLevel.isCritical(),
                algorithmLevel.isCritical() ? algorithmLevel.advices() : null
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CriticalCheckAssesmentOutput that = (CriticalCheckAssesmentOutput) o;
        return hasCriticalState == that.hasCriticalState &&
                Objects.equals(advices, that.advices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hasCriticalState, advices);
    }

    @Override
    public String toString() {
        return "CriticalCheckAssesmentOutput{" +
                "hasCriticalState=" + hasCriticalState +
                ", advices=" + advices +
                '}';
    }
}
