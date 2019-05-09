package tech.sosa.triage_assistance_service.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class TriageAlgorithmDTO {

    public String emergencyLevel;
    @JsonInclude(Include.NON_NULL)
    public Collection<DiscriminatorDTO> discriminators;
    @JsonInclude(Include.NON_NULL)
    public Collection<String> advices;
    @JsonInclude(Include.NON_NULL)
    public TriageAlgorithmDTO lowerLevel;

    public TriageAlgorithmDTO() {
    }

    public TriageAlgorithmDTO(String emergencyLevel,
            Collection<DiscriminatorDTO> discriminators, Collection<String> advices,
            TriageAlgorithmDTO lowerLevel) {
        this.emergencyLevel = emergencyLevel;
        this.discriminators = discriminators;
        this.advices = advices;
        this.lowerLevel = lowerLevel;
    }

    public String emergencyLevel() {
        return emergencyLevel;
    }

    public void emergencyLevel(String emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public Optional<Collection<DiscriminatorDTO>> discriminators() {
        return Optional.ofNullable(discriminators);
    }

    public void discriminators(Collection<DiscriminatorDTO> discriminators) {
        this.discriminators = discriminators;
    }

    public Optional<Collection<String>> advices() {
        return Optional.ofNullable(advices);
    }

    public void advices(Collection<String> advices) {
        this.advices = advices;
    }

    public Optional<TriageAlgorithmDTO> lowerLevel() {
        return Optional.ofNullable(lowerLevel);
    }

    public void lowerLevel(TriageAlgorithmDTO lowerLevel) {
        this.lowerLevel = lowerLevel;
    }

    @Override
    public String toString() {
        return "TriageAlgorithmDTO{" +
                "emergencyLevel='" + emergencyLevel + '\'' +
                ", discriminators=" + discriminators +
                ", advices=" + advices +
                ", lowerLevel=" + lowerLevel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TriageAlgorithmDTO that = (TriageAlgorithmDTO) o;
        return Objects.equals(emergencyLevel, that.emergencyLevel) &&
                Objects.equals(discriminators, that.discriminators) &&
                Objects.equals(advices, that.advices) &&
                Objects.equals(lowerLevel, that.lowerLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emergencyLevel, discriminators, advices, lowerLevel);
    }
}
