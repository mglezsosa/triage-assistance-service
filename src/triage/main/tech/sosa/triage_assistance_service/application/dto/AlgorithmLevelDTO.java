package tech.sosa.triage_assistance_service.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Collection;
import java.util.Objects;

public class AlgorithmLevelDTO {

    public String levelTitle;
    public Collection<String> advices;

    @JsonInclude(Include.NON_NULL)
    public Collection<DiscriminatorDTO> discriminators;

    public AlgorithmLevelDTO() {
    }

    public AlgorithmLevelDTO(String levelTitle, Collection<String> advices,
            Collection<DiscriminatorDTO> discriminators) {
        this.levelTitle = levelTitle;
        this.advices = advices;
        this.discriminators = discriminators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AlgorithmLevelDTO that = (AlgorithmLevelDTO) o;
        return Objects.equals(levelTitle, that.levelTitle) &&
                Objects.equals(advices, that.advices) &&
                Objects.equals(discriminators, that.discriminators);
    }

    @Override
    public int hashCode() {
        return Objects.hash(levelTitle, advices, discriminators);
    }

    @Override
    public String toString() {
        return "AlgorithmLevelDTO{" +
                "levelTitle='" + levelTitle + '\'' +
                ", advices=" + advices +
                ", discriminators=" + discriminators +
                '}';
    }
}
