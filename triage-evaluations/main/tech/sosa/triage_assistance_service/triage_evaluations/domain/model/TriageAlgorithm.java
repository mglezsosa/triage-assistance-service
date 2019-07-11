package tech.sosa.triage_assistance_service.triage_evaluations.domain.model;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public AlgorithmLevel evaluate(Collection<ClinicalFinding> findings) {
        return firstLevelThatContains(findings);
    }

    private AlgorithmLevel firstLevelThatContains(Collection<ClinicalFinding> findings) {
        return levels.stream()
                .filter(level -> level.containsAny(findings)).findFirst().orElseThrow();
    }

    public TriageAlgorithm criticalStateSubalgorithm() {
        List<AlgorithmLevel> selectedLevels = levels.stream().filter(AlgorithmLevel::isCritical)
                .collect(Collectors.toList());
        selectedLevels.add(levels.get(levels.size() - 1));
        return new TriageAlgorithm(selectedLevels);
    }

}
