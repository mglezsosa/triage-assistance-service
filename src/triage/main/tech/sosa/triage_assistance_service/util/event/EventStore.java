package tech.sosa.triage_assistance_service.util.event;

public interface EventStore<T extends Event> {

    void store(T event);

}
