package tech.sosa.triage_assistance_service.applications.infrastructure.springframework.boot.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.rabbitmq.client.Channel;
import org.springframework.core.annotation.Order;
import tech.sosa.triage_assistance_service.applications.port.adapter.RabbitMQQueueSizeChangedNotificationSubscriber;
import tech.sosa.triage_assistance_service.applications.util.MultiReadHttpServletRequest;
import tech.sosa.triage_assistance_service.identity_access.domain.event.AuthorizationCheckedSubscriber;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.AuditingCriticalCheckTriageAssessedSubscriber;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.AuditingFullTriageAssessmentSubscriber;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.shared.domain.event.EventPublisher;
import tech.sosa.triage_assistance_service.shared.domain.event.EventStore;

@Order(1)
public class EventPublisherResetFilter implements Filter {

    private EventStore eventStore;
    private PendingTriagesQueue queue;
    private Channel rabbitMQChannel;

    public EventPublisherResetFilter(
            EventStore eventStore,
            PendingTriagesQueue queue,
            Channel rabbitMQChannel) {
        this.eventStore = eventStore;
        this.queue = queue;
        this.rabbitMQChannel = rabbitMQChannel;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        EventPublisher.instance().reset();

        EventPublisher.instance().subscribe(new AuthorizationCheckedSubscriber(eventStore));
        EventPublisher.instance().subscribe(new AuditingCriticalCheckTriageAssessedSubscriber(eventStore));
        EventPublisher.instance().subscribe(new AuditingFullTriageAssessmentSubscriber(eventStore));
        EventPublisher.instance().subscribe(new RabbitMQQueueSizeChangedNotificationSubscriber(rabbitMQChannel, queue));

        chain.doFilter(new MultiReadHttpServletRequest((HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {

    }
}
