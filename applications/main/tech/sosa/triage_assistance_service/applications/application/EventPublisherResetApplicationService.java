package tech.sosa.triage_assistance_service.applications.application;

import com.rabbitmq.client.Channel;
import tech.sosa.triage_assistance_service.applications.port.adapter.RabbitMQQueueSizeChangedNotificationSubscriber;
import tech.sosa.triage_assistance_service.identity_access.domain.event.AuthorizationCheckedSubscriber;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationRequest;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;
import tech.sosa.triage_assistance_service.shared.domain.event.EventPublisher;
import tech.sosa.triage_assistance_service.shared.domain.event.EventStore;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.AuditingCriticalCheckTriageAssessedSubscriber;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.AuditingFullTriageAssessmentSubscriber;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;

public class EventPublisherResetApplicationService<S, T extends ApplicationRequest> implements ApplicationService<S, T> {

    private ApplicationService<S, T> proxiedApplicationService;
    private EventStore eventStore;
    private PendingTriagesQueue queue;
    private Channel rabbitMQChannel;

    public EventPublisherResetApplicationService(
            ApplicationService<S, T> proxiedApplicationService,
            EventStore eventStore,
            PendingTriagesQueue queue,
            Channel rabbitMQChannel) {
        this.proxiedApplicationService = proxiedApplicationService;
        this.eventStore = eventStore;
        this.queue = queue;
        this.rabbitMQChannel = rabbitMQChannel;
    }

    @Override
    public S execute(T request) {
        EventPublisher.instance().reset();
        EventPublisher.instance().subscribe(new AuthorizationCheckedSubscriber(eventStore));
        EventPublisher.instance().subscribe(new AuditingCriticalCheckTriageAssessedSubscriber(eventStore));
        EventPublisher.instance().subscribe(new AuditingFullTriageAssessmentSubscriber(eventStore));
        EventPublisher.instance().subscribe(new RabbitMQQueueSizeChangedNotificationSubscriber(rabbitMQChannel, queue));

        return proxiedApplicationService.execute(request);
    }
}
