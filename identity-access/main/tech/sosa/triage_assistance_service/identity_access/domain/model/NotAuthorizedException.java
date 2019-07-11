package tech.sosa.triage_assistance_service.identity_access.domain.model;

public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException() {
    }

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAuthorizedException(Throwable cause) {
        super(cause);
    }
}
