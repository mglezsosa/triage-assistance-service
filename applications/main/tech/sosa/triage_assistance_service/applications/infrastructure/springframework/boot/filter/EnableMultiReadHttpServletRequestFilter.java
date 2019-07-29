package tech.sosa.triage_assistance_service.applications.infrastructure.springframework.boot.filter;

import tech.sosa.triage_assistance_service.applications.util.MultiReadHttpServletRequest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class EnableMultiReadHttpServletRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest(
                (HttpServletRequest) request);
        chain.doFilter(multiReadRequest, response);
    }

    @Override
    public void destroy() {

    }
}
