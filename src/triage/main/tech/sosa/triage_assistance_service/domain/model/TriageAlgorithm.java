package tech.sosa.triage_assistance_service.domain.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class TriageAlgorithm {

    private List<AlgorithmLevel> levels;

    public TriageAlgorithm(List<AlgorithmLevel> levels) {
        this.levels = levels;
    }

    public List<AlgorithmLevel> levels() {
        return levels;
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
        return Objects.equals(levels, algorithm.levels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(levels);
    }

    @Override
    public String toString() {
        return "TriageAlgorithm{" +
                "levels=" + levels +
                '}';
    }

    public TriageOutput evaluate(Collection<ClinicalFinding> findings) {
        AlgorithmLevel resultLevel = firstLevelThatContains(findings);
        return new TriageOutput(
                resultLevel.title(),
                resultLevel.advices()
        );
    }

    private AlgorithmLevel firstLevelThatContains(Collection<ClinicalFinding> findings) {
        return levels.stream()
                .filter(level -> level.containsAny(findings)).findFirst().orElseThrow();
    }

}
