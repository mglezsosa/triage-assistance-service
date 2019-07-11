package tech.sosa.triage_assistance_service.triage_evaluations.domain.model;

public class TriageAlreadyExistsException extends RuntimeException {

    private TriageAlreadyExistsException(String message) {
        super(message);
    }

    public static TriageAlreadyExistsException withChiefComplaint(ChiefComplaint chiefComplaint) {
        return new TriageAlreadyExistsException(
                String.format("Triage with chief compaint with id '%s' already exists.",
                        chiefComplaint.id.toString())
        );
    }
}
