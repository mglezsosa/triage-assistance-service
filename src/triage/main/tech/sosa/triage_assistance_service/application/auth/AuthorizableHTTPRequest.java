package tech.sosa.triage_assistance_service.application.auth;

import java.util.Objects;

public class AuthorizableHTTPRequest {

    private String URI;
    private String method;

    public AuthorizableHTTPRequest(String URI, String method) {
        this.URI = URI;
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorizableHTTPRequest that = (AuthorizableHTTPRequest) o;
        return Objects.equals(URI, that.URI) &&
                Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(URI, method);
    }
}
