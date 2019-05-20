package tech.sosa.triage_assistance_service.application.dto;

import java.util.Objects;

public class TriageDTO {

    public ClinicalFindingDTO chiefComplaint;
    public AlgorithmDTO algorithm;

    public TriageDTO() {
    }

    public TriageDTO(
            ClinicalFindingDTO chiefComplaint,
            AlgorithmDTO algorithm) {
        this.chiefComplaint = chiefComplaint;
        this.algorithm = algorithm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TriageDTO triageDTO = (TriageDTO) o;
        return Objects.equals(chiefComplaint, triageDTO.chiefComplaint) &&
                Objects.equals(algorithm, triageDTO.algorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chiefComplaint, algorithm);
    }

    @Override
    public String toString() {
        return "TriageDTO{" +
                "chiefComplaint=" + chiefComplaint +
                ", algorithm=" + algorithm +
                '}';
    }
}
