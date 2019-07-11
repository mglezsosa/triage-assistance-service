package tech.sosa.triage_assistance_service.shared.domain.event;

public interface EventStore<T extends Event> {

    void store(T event);

}
