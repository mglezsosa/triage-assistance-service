package tech.sosa.triage_assistance_service.infrastructure.springframework.boot.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import tech.sosa.triage_assistance_service.application.auth.AuthService;
import tech.sosa.triage_assistance_service.application.auth.User;
import tech.sosa.triage_assistance_service.application.event.AuthenticatedEndpointReached;
import tech.sosa.triage_assistance_service.infrastructure.util.MultiReadHttpServletRequest;
import tech.sosa.triage_assistance_service.util.event.EventPublisher;

@Order(2)
public class AuthenticationFilter implements Filter {

    private AuthService authService;

    public AuthenticationFilter(
            AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest multiReadRequest = new MultiReadHttpServletRequest(
                (HttpServletRequest) request);

        User user = authService.authenticate(multiReadRequest);

        EventPublisher.instance().publish(
                new AuthenticatedEndpointReached(
                        user,
                        multiReadRequest
                )
        );

        chain.doFilter(multiReadRequest, response);
    }

    @Override
    public void destroy() {

    }
}
