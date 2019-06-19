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
import tech.sosa.triage_assistance_service.application.event.AuditingEndpointSubscriber;
import tech.sosa.triage_assistance_service.domain.event.AuditingCriticalCheckTriageAssessedSubscriber;
import tech.sosa.triage_assistance_service.domain.event.AuditingFullTriageAssessmentSubscriber;
import tech.sosa.triage_assistance_service.domain.event.NonCriticalAssessingEqueuerSubscriber;
import tech.sosa.triage_assistance_service.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.infrastructure.util.MultiReadHttpServletRequest;
import tech.sosa.triage_assistance_service.util.event.EventPublisher;
import tech.sosa.triage_assistance_service.util.event.EventStore;

@Order(1)
public class EventPublisherResetFilter implements Filter {

    private EventStore eventStore;
    private PendingTriagesQueue queue;

    public EventPublisherResetFilter(
            EventStore eventStore,
            PendingTriagesQueue queue) {
        this.eventStore = eventStore;
        this.queue = queue;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        EventPublisher.instance().reset();

        EventPublisher.instance().subscribe(new AuditingEndpointSubscriber(eventStore));
        EventPublisher.instance().subscribe(new AuditingCriticalCheckTriageAssessedSubscriber(eventStore));
        EventPublisher.instance().subscribe(new AuditingFullTriageAssessmentSubscriber(eventStore));
        EventPublisher.instance().subscribe(new NonCriticalAssessingEqueuerSubscriber(queue));

        chain.doFilter(new MultiReadHttpServletRequest((HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {

    }
}
