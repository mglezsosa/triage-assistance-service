package tech.sosa.triage_assistance_service.application.dto;

import java.util.Collection;
import java.util.Objects;

public class DiscriminatorDTO {

    public String id;
    public Collection<String> questions;
    public String definition;

    public DiscriminatorDTO() {
    }

    public DiscriminatorDTO(String id, Collection<String> questions, String definition) {
        this.id = id;
        this.questions = questions;
        this.definition = definition;
    }

    public String id() {
        return id;
    }

    public void id(String id) {
        this.id = id;
    }

    public Collection<String> questions() {
        return questions;
    }

    public void questions(Collection<String> questions) {
        this.questions = questions;
    }

    public String definition() {
        return definition;
    }

    public void definition(String definition) {
        this.definition = definition;
    }

    @Override
    public String toString() {
        return "DiscriminatorDTO{" +
                "id='" + id + '\'' +
                ", questions=" + questions +
                ", definition='" + definition + '\'' +
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
        DiscriminatorDTO that = (DiscriminatorDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(questions, that.questions) &&
                Objects.equals(definition, that.definition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questions, definition);
    }
}
