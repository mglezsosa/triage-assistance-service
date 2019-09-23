package tech.sosa.triage_assistance_service.triage_evaluations.port.adapter;

import tech.sosa.triage_assistance_service.shared.domain.event.Event;
import tech.sosa.triage_assistance_service.shared.domain.event.EventStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InMemoryEventStore implements EventStore {

    private Collection<Event> events;

    public InMemoryEventStore() {
        this.events = new ArrayList<>();
    }

    @Override
    public void store(Event event) {
        events.add(event);
    }

    public List<Event> events() {
        return new ArrayList<>(this.events);
    }
}
