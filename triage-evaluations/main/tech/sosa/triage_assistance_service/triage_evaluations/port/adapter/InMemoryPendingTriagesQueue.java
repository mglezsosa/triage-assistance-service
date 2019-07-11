package tech.sosa.triage_assistance_service.triage_evaluations.port.adapter;

import java.util.List;

import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.StatefulCriticalCheckTriageAssessed;

public class InMemoryPendingTriagesQueue implements PendingTriagesQueue {

    private List<StatefulCriticalCheckTriageAssessed> events;

    public InMemoryPendingTriagesQueue(
            List <StatefulCriticalCheckTriageAssessed> events) {
        this.events = events;
    }

    @Override
    public void enqueue(CriticalCheckTriageAssessed assessing) {
        events.add(StatefulCriticalCheckTriageAssessed.create(assessing));
    }

    @Override
    public CriticalCheckTriageAssessed findInProcessOrFail(String id) {
        return events.stream().filter(e -> e.getId().equals(id) && e.getStatus() == StatefulCriticalCheckTriageAssessed.Status.IN_PROCESS)
                .findFirst().orElseThrow();
    }

    @Override
    public CriticalCheckTriageAssessed nextPending() {
        StatefulCriticalCheckTriageAssessed assessing = events.stream().filter(e ->
                e.getStatus() == StatefulCriticalCheckTriageAssessed.Status.PENDING).findFirst().orElseThrow();
        assessing.startProcessing();
        return assessing;
    }

    @Override
    public void finish(CriticalCheckTriageAssessed assessing) {
        events.removeIf(e -> e.getId().equals(assessing.getId()));
    }

}
