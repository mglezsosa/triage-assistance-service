package tech.sosa.triage_assistance_service.application.dto;

import java.util.Collection;
import java.util.Objects;

public class DiscriminatorDTO extends ClinicalFindingDTO {

    public String definition;
    public Collection<String> questions;

    public DiscriminatorDTO() {
    }

    public DiscriminatorDTO(String expression, String title, String definition,
            Collection<String> questions) {
        super(expression, title);
        this.definition = definition;
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        DiscriminatorDTO that = (DiscriminatorDTO) o;
        return Objects.equals(definition, that.definition) &&
                Objects.equals(questions, that.questions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), definition, questions);
    }

    @Override
    public String toString() {
        return "DiscriminatorDTO{" +
                "definition='" + definition + '\'' +
                ", questions=" + questions +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
