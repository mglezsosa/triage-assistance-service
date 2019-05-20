package tech.sosa.triage_assistance_service.application.dto;

import java.util.List;
import java.util.Objects;

public class AlgorithmDTO {

    public List<AlgorithmLevelDTO> criticalLevels;
    public List<AlgorithmLevelDTO> intermediateLevels;
    public AlgorithmLevelDTO defaultLevel;

    public AlgorithmDTO() {
    }

    public AlgorithmDTO(
            List<AlgorithmLevelDTO> criticalLevels,
            List<AlgorithmLevelDTO> intermediateLevels,
            AlgorithmLevelDTO defaultLevel) {
        this.criticalLevels = criticalLevels;
        this.intermediateLevels = intermediateLevels;
        this.defaultLevel = defaultLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AlgorithmDTO that = (AlgorithmDTO) o;
        return Objects.equals(criticalLevels, that.criticalLevels) &&
                Objects.equals(intermediateLevels, that.intermediateLevels) &&
                Objects.equals(defaultLevel, that.defaultLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(criticalLevels, intermediateLevels, defaultLevel);
    }

    @Override
    public String toString() {
        return "AlgorithmDTO{" +
                "criticalLevels=" + criticalLevels +
                ", intermediateLevels=" + intermediateLevels +
                ", defaultLevel=" + defaultLevel +
                '}';
    }
}
