package tech.sosa.triage_assistance_service.application.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;
import tech.sosa.triage_assistance_service.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.domain.model.ClinicalFindingTitle;
import tech.sosa.triage_assistance_service.domain.model.CriticalCheckAssesmentOutput;
import tech.sosa.triage_assistance_service.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.port.adapter.InMemoryPendingTriagesQueue;
import tech.sosa.triage_assistance_service.utils.TestWithUtils;

public class NextEnqueuedTriageShould extends TestWithUtils {

    private PendingTriagesQueue queue;

    @Before
    public void setUp() {
        queue = new InMemoryPendingTriagesQueue(new ArrayList<>());
    }

    @Test
    public void fetchNextPendingAssessmentWithNonEmptyQueue() {

        queue.enqueue(new CriticalCheckTriageAssessed("1234",
                        new ChiefComplaint(
                                new ClinicalFindingId("21522001:246454002=41847000"),
                                new ClinicalFindingTitle("Dolor abdominal en adultos")),
                        new CriticalCheckAssesmentOutput(false, null)
                )
        );

        CriticalCheckTriageAssessed enqueuedPreTriage = new NextEnqueuedTriage(queue).execute(null);

        assertEquals("1234", enqueuedPreTriage.getId());
    }

    @Test(expected = NoSuchElementException.class)
    public void fetchNextPendingAssessmentWithEmptyQueue() {
        new NextEnqueuedTriage(queue).execute(null);
    }

}
