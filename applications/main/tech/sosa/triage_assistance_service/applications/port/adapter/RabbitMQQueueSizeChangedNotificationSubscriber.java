package tech.sosa.triage_assistance_service.applications.port.adapter;

import com.rabbitmq.client.Channel;
import tech.sosa.triage_assistance_service.shared.domain.event.EventSubscriber;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.PendingCasesQueueSizeChanged;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;

import java.io.IOException;

public class RabbitMQQueueSizeChangedNotificationSubscriber implements EventSubscriber<PendingCasesQueueSizeChanged> {

    private final Channel rabbitMQClient;
    private final PendingTriagesQueue queue;

    public RabbitMQQueueSizeChangedNotificationSubscriber(Channel rabbitMQClient, PendingTriagesQueue queue) {
        this.rabbitMQClient = rabbitMQClient;
        this.queue = queue;
    }

    @Override
    public void handleEvent(PendingCasesQueueSizeChanged anEvent) {
        long queueSize = queue.numberOfEnqueuedCases();
        try {
            rabbitMQClient.basicPublish(
                    "amq.topic",
                    "queue-size",
                    null,
                    Long.toString(queueSize).getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Couldn't publish queue size notification.");
        }
    }

    @Override
    public Class<PendingCasesQueueSizeChanged> subscribedToEventType() {
        return PendingCasesQueueSizeChanged.class;
    }
}
