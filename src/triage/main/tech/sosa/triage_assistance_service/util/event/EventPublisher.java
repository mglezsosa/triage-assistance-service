package tech.sosa.triage_assistance_service.util.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventPublisher {

    private static final ThreadLocal<EventPublisher> instance = new ThreadLocal<EventPublisher>() {
        protected EventPublisher initialValue() {
            return new EventPublisher();
        }
    };

    private boolean publishing;

    @SuppressWarnings("rawtypes")
    private List subscribers;

    public static EventPublisher instance() {
        return instance.get();
    }

    public <T extends Event> void publish(final T anEvent) {
        if (!this.isPublishing() && this.hasSubscribers()) {

            try {
                this.setPublishing(true);

                Class<?> eventType = anEvent.getClass();

                @SuppressWarnings("unchecked")
                List<EventSubscriber<T>> allSubscribers = this.subscribers();

                for (EventSubscriber<T> subscriber : allSubscribers) {
                    Class<?> subscribedToType = subscriber.subscribedToEventType();

                    if (eventType == subscribedToType || subscribedToType == Event.class) {
                        subscriber.handleEvent(anEvent);
                    }
                }

            } finally {
                this.setPublishing(false);
            }
        }
    }

    public void publishAll(Collection<Event> events) {
        for (Event event : events) {
            this.publish(event);
        }
    }

    public void reset() {
        if (!this.isPublishing()) {
            this.setSubscribers(null);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void subscribe(EventSubscriber<T> aSubscriber) {
        if (!this.isPublishing()) {
            this.ensureSubscribersList();

            this.subscribers().add(aSubscriber);
        }
    }

    private EventPublisher() {
        super();

        this.setPublishing(false);
        this.ensureSubscribersList();
    }

    @SuppressWarnings("rawtypes")
    private void ensureSubscribersList() {
        if (!this.hasSubscribers()) {
            this.setSubscribers(new ArrayList());
        }
    }

    private boolean isPublishing() {
        return this.publishing;
    }

    private void setPublishing(boolean aFlag) {
        this.publishing = aFlag;
    }

    private boolean hasSubscribers() {
        return this.subscribers() != null;
    }

    @SuppressWarnings("rawtypes")
    private List subscribers() {
        return this.subscribers;
    }

    @SuppressWarnings("rawtypes")
    private void setSubscribers(List aSubscriberList) {
        this.subscribers = aSubscriberList;
    }
}
