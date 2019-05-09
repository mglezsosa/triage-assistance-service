package tech.sosa.triage_assistance_service.application.dto;

import java.util.Objects;

public class TriageDTO {

    public String chiefComplaint;
    public TriageAlgorithmDTO algorithm;

    public String chiefComplaint() {
        return chiefComplaint;
    }

    public void chiefComplaint(String chiefComplaint) {
        this.chiefComplaint = chiefComplaint;
    }

    public TriageAlgorithmDTO algorithm() {
        return algorithm;
    }

    public void algorithm(TriageAlgorithmDTO algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String toString() {
        return "TriageDTO{" +
                "chiefComplaint='" + chiefComplaint + '\'' +
                ", algorithm=" + algorithm +
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
        TriageDTO triageDTO = (TriageDTO) o;
        return Objects.equals(chiefComplaint, triageDTO.chiefComplaint) &&
                Objects.equals(algorithm, triageDTO.algorithm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chiefComplaint, algorithm);
    }
}
