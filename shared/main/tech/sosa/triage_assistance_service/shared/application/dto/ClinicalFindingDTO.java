package tech.sosa.triage_assistance_service.shared.application.dto;

import java.util.Objects;

public class ClinicalFindingDTO {

    public String id;
    public String title;

    public ClinicalFindingDTO() {
    }

    public ClinicalFindingDTO(String id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClinicalFindingDTO that = (ClinicalFindingDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "ClinicalFindingDTO{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
