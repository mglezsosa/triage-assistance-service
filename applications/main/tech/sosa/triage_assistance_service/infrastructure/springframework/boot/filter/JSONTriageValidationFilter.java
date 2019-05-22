package tech.sosa.triage_assistance_service.infrastructure.springframework.boot.filter;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import tech.sosa.triage_assistance_service.infrastructure.util.MultiReadHttpServletRequest;


@Order(1)
public class JSONTriageValidationFilter implements Filter {

    private Schema jsonSchema;

    public JSONTriageValidationFilter(Schema jsonSchema) {
        this.jsonSchema = jsonSchema;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        MultiReadHttpServletRequest multiReadRequest = new MultiReadHttpServletRequest(
                (HttpServletRequest) request);
        if (Arrays.asList("POST", "PUT").contains(multiReadRequest.getMethod())) {
            try {
                var json = new JSONObject(
                        new JSONTokener(multiReadRequest.getInputStream())
                );
                jsonSchema.validate(json.getJSONObject("triage"));
            } catch (ValidationException e) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.setStatus(HttpStatus.BAD_REQUEST.value());
                res.setHeader("Content-type", "application/json");
                res.getWriter().write(e.toJSON().toString());
            }
        }
        chain.doFilter(multiReadRequest, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

}