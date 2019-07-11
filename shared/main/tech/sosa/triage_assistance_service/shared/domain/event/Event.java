package tech.sosa.triage_assistance_service.shared.domain.event;

import java.time.LocalDateTime;

public abstract class Event {

    protected LocalDateTime occurredOn;

    public Event() {
        this.occurredOn = LocalDateTime.now();
    }

    public LocalDateTime occurredOn() {
        return occurredOn;
    }

}
