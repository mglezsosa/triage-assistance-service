package tech.sosa.triage_assistance_service.applications.everit.json.schema;

import java.util.Optional;
import org.everit.json.schema.FormatValidator;
import org.snomed.languages.scg.SCGException;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFindingId;

public class SCGExpressionValidator implements FormatValidator {

    @Override
    public Optional<String> validate(String subject) {
        try {
            new ClinicalFindingId(subject);
        } catch (SCGException e) {
            return Optional.of(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public String formatName() {
        return "snomed-ct-expression";
    }
}
