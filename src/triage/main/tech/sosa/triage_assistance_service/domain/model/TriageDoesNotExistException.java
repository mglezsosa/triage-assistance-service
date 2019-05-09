package tech.sosa.triage_assistance_service.domain.model;

public class TriageDoesNotExistException extends RuntimeException {

    private TriageDoesNotExistException(String message) {
        super(message);
    }

    public static TriageDoesNotExistException withChiefComplaint(ChiefComplaint chiefComplaint) {
        return new TriageDoesNotExistException(
                String.format("Triage with chief compaint %s does not exist.", chiefComplaint)
        );
    }
}
