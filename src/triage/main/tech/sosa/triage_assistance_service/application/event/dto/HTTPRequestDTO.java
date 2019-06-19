package tech.sosa.triage_assistance_service.application.event.dto;

import java.util.Collection;

public class HTTPRequestDTO {

    public final String method;
    public final String URI;
    public final Collection<String> headers;
    public final String body;

    public HTTPRequestDTO(String method, String URI, Collection<String> headers, String body) {
        this.method = method;
        this.URI = URI;
        this.headers = headers;
        this.body = body;
    }
}
