package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import org.junit.Before;
import org.junit.Test;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.*;
import tech.sosa.triage_assistance_service.triage_evaluations.port.adapter.InMemoryPendingTriagesQueue;
import tech.sosa.triage_assistance_service.triage_evaluations.utils.TestWithUtils;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ViewNumberOfEqueuedCasesShould extends TestWithUtils {

    private PendingTriagesQueue queue;

    @Before
    public void setUp() {
        queue = new InMemoryPendingTriagesQueue(new ArrayList<>());
    }

    @Test
    public void fetchNumberOfEnqueuedCasesWithNonEmptyQueue() {

        queue.enqueue(new CriticalCheckTriageAssessed("1234",
                        new ChiefComplaint(
                                new ClinicalFindingId("21522001:246454002=41847000"),
                                new ClinicalFindingTitle("Dolor abdominal en adultos")),
                        new CriticalCheckAssesmentOutput(false, null)
                )
        );

        long numberOfEnqueuedCases = new ViewNumberOfEqueuedCases(queue).execute(null);

        assertEquals(1, numberOfEnqueuedCases);
    }

    @Test
    public void fetchNumberOfEnqueuedCasesWithEmptyQueue() {

        long numberOfEnqueuedCases = new ViewNumberOfEqueuedCases(queue).execute(null);

        assertEquals(0, numberOfEnqueuedCases);
    }
}
