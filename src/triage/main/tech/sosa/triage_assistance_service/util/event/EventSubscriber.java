package tech.sosa.triage_assistance_service.util.event;

public interface EventSubscriber<T extends Event> {

    void handleEvent(final T anEvent);

    Class<T> subscribedToEventType();

}