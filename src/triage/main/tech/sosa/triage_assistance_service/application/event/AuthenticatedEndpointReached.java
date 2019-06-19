package tech.sosa.triage_assistance_service.application.event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import tech.sosa.triage_assistance_service.application.auth.User;
import tech.sosa.triage_assistance_service.application.event.dto.HTTPRequestDTO;
import tech.sosa.triage_assistance_service.util.event.Event;

public class AuthenticatedEndpointReached extends Event {

    private User user;
    private HttpServletRequest request;

    public AuthenticatedEndpointReached(User user, HttpServletRequest request) {
        super();
        this.user = user;
        this.request = request;
    }

    public User getUser() {
        return user;
    }

    public HTTPRequestDTO getRequest() throws IOException {
        Collection<String> headersCollection = new ArrayList<>();

        for (Iterator it = request.getHeaderNames().asIterator(); it.hasNext(); ) {
            String name = (String) it.next();
            headersCollection.add(name + " " + request.getHeader(name));
        }

        return new HTTPRequestDTO(
                request.getMethod(),
                request.getRequestURI(),
                headersCollection,
                new BufferedReader(new InputStreamReader(request.getInputStream()))
                        .lines().collect(Collectors.joining("\n"))
        );
    }
}
