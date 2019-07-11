package tech.sosa.triage_assistance_service.triage_evaluations.domain.model;

import java.util.Collection;
import java.util.Objects;

public class Discriminator extends ClinicalFinding {

    private Collection<String> questions;
    private String definition;

    public Discriminator(ClinicalFindingId id,
            ClinicalFindingTitle title, Collection<String> questions, String definition) {
        super(id, title);
        this.questions = questions;
        this.definition = definition;
    }

    public Collection<String> questions() {
        return questions;
    }

    public String definition() {
        return definition;
    }

    @Override
    public String toString() {
        return "{ id: " + id() + ", "
                + "questions: " + questions.toString() + ", "
                + "definition: " + definition + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || (getClass() != o.getClass()
                && !o.getClass().equals(this.getClass().getSuperclass()))) {
            return false;
        }
        ClinicalFinding cf = (ClinicalFinding) o;
        return id.equals(cf.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), questions, definition);
    }
}
