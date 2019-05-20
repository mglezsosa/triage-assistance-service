package tech.sosa.triage_assistance_service.domain.model;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class AlgorithmLevel {

    private AlgorithmLevelTitle title;
    private Collection<Discriminator> discriminators;
    private Collection<String> advices;
    private boolean isCritical;

    public AlgorithmLevel(AlgorithmLevelTitle title, Collection<Discriminator> discriminators,
            Collection<String> advices, boolean isCritical) {
        this.title = title;
        this.discriminators = discriminators;
        this.advices = advices;
        this.isCritical = isCritical;
    }

    public AlgorithmLevel(AlgorithmLevelTitle title, Collection<String> advices) {
        this.title = title;
        this.advices = advices;
        this.isCritical = false;
    }

    public AlgorithmLevelTitle title() {
        return title;
    }

    public Collection<Discriminator> discriminators() {
        return discriminators;
    }

    public Collection<String> advices() {
        return advices;
    }

    public boolean isCritical() {
        return isCritical;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AlgorithmLevel that = (AlgorithmLevel) o;
        return isCritical == that.isCritical &&
                Objects.equals(title, that.title) &&
                Objects.equals(discriminators, that.discriminators) &&
                Objects.equals(advices, that.advices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, discriminators, advices, isCritical);
    }

    @Override
    public String toString() {
        return "AlgorithmLevel{" +
                "title=" + title +
                ", discriminators=" + discriminators +
                ", advices=" + advices +
                ", isCritical=" + isCritical +
                '}';
    }

    public boolean containsAny(Collection<ClinicalFinding> clinicalFindings) {
        return Optional.ofNullable(discriminators)
                .map(ds -> ds.stream().anyMatch(clinicalFindings::contains))
                .orElse(true);
    }
}
