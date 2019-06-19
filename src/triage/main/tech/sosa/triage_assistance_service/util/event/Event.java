package tech.sosa.triage_assistance_service.util.event;

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
