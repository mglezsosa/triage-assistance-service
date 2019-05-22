package tech.sosa.triage_assistance_service.domain.model;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.snomed.languages.scg.SCGObjectFactory;
import org.snomed.languages.scg.SCGQueryBuilder;
import org.snomed.languages.scg.domain.model.Attribute;
import org.snomed.languages.scg.domain.model.Expression;

public class ClinicalFindingId {

    private Expression value;

    public ClinicalFindingId(String value) {
        SCGQueryBuilder builder = new SCGQueryBuilder(new SCGObjectFactory());
        this.value = builder.createQuery(value);
    }

    public String value() {
        return toString(value);
    }

    private String toString(Expression expression) {
        return String.join("+", expression.getFocusConcepts())
                + Optional.ofNullable(expression.getAttributes())
                .map(attrs -> ":" + attrs.stream().map(this::toString)
                        .collect(Collectors.joining(",")))
                .orElse("")
                + Optional.ofNullable(expression.getAttributeGroups())
                .map(ags -> (expression.getAttributes() == null ? ":" : ",")
                        + ags.stream()
                        .map(ag -> "{" + ag.getAttributes().stream().map(this::toString)
                                .collect(Collectors.joining(",")) + "}")
                        .collect(Collectors.joining(",")))
                .orElse("");
    }

    private String toString(Attribute attribute) {
        String auxStr = attribute.getAttributeId() + "=";
        if (attribute.getAttributeValue().isNested()) {
            return auxStr + "(" + toString(attribute.getAttributeValue().getNestedExpression())
                    + ")";
        }
        return auxStr + attribute.getAttributeValue().getConceptId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClinicalFindingId that = (ClinicalFindingId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value();
    }
}
