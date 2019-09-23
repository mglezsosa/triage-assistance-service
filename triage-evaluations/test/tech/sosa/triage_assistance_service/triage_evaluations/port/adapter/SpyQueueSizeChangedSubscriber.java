package tech.sosa.triage_assistance_service.triage_evaluations.port.adapter;

import tech.sosa.triage_assistance_service.shared.domain.event.EventSubscriber;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.PendingCasesQueueSizeChanged;

public class SpyQueueSizeChangedSubscriber implements EventSubscriber<PendingCasesQueueSizeChanged> {

    private int numberOfHandleEventCalls;

    @Override
    public void handleEvent(PendingCasesQueueSizeChanged anEvent) {
        numberOfHandleEventCalls++;
    }

    @Override
    public Class<PendingCasesQueueSizeChanged> subscribedToEventType() {
        return PendingCasesQueueSizeChanged.class;
    }

    public int getNumberOfHandleEventCalls() {
        return  numberOfHandleEventCalls;
    }
}
