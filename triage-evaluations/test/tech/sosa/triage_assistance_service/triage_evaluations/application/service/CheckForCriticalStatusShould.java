package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;
import org.snomed.languages.scg.SCGException;
import tech.sosa.triage_assistance_service.shared.domain.event.EventPublisher;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.shared.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.AuditingCriticalCheckTriageAssessedSubscriber;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.AuditingFullTriageAssessmentSubscriber;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.FullTriageAssessed;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.CriticalCheckAssesmentOutput;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageDoesNotExistException;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;
import tech.sosa.triage_assistance_service.triage_evaluations.persistence.TriageRepositoryStub;
import tech.sosa.triage_assistance_service.triage_evaluations.port.adapter.InMemoryEventStore;
import tech.sosa.triage_assistance_service.triage_evaluations.port.adapter.InMemoryPendingTriagesQueue;
import tech.sosa.triage_assistance_service.triage_evaluations.port.adapter.SpyQueueSizeChangedSubscriber;
import tech.sosa.triage_assistance_service.triage_evaluations.utils.TestWithUtils;

public class CheckForCriticalStatusShould extends TestWithUtils {

    private TriageRepository repository;
    private PendingTriagesQueue queue;
    private InMemoryEventStore eventStore;
    private SpyQueueSizeChangedSubscriber spyQueueSizeChangedSubscriber;

    @Before
    public void setUp() throws IOException, URISyntaxException {
        TriageMapper mapper = new TriageMapper();
        repository = TriageRepositoryStub.with(mapper.from(readJSON(readFromResource("triageExample.json"),
                TriageDTO.class)));
        queue = new InMemoryPendingTriagesQueue(new ArrayList<>());
        eventStore = new InMemoryEventStore();
        spyQueueSizeChangedSubscriber = new SpyQueueSizeChangedSubscriber();

        EventPublisher.instance().reset();
        EventPublisher.instance().subscribe(new AuditingCriticalCheckTriageAssessedSubscriber(eventStore));
        EventPublisher.instance().subscribe(spyQueueSizeChangedSubscriber);
    }

    @Test(expected = SCGException.class)
    public void throw_a_exception_if_triage_id_is_not_a_valid_snomed_expression() {
        new CheckForCriticalState(repository, queue)
                .execute(new CheckForCriticalStateRequest(
                        "2152200112344455",
                        Collections.emptyList()
                ));
    }

    @Test(expected = TriageDoesNotExistException.class)
    public void throw_a_exception_if_selected_triage_does_not_exist() {
        new CheckForCriticalState(repository, queue)
                .execute(new CheckForCriticalStateRequest(
                        "21522001",
                        Collections.emptyList()
                ));
    }

    @Test
    public void return_falsy_output_with_no_findings() {
        CriticalCheckAssesmentOutput actualOutput = new CheckForCriticalState(repository, queue)
                .execute(new CheckForCriticalStateRequest(
                        "21522001:246454002=41847000",
                        Collections.emptyList()
                ));

        assertEquals(
                new CriticalCheckAssesmentOutput(false, null),
                actualOutput
        );

        assertEquals(actualOutput, queue.nextPending().getOutput());

        assertEquals(eventStore.events().size(), 1);
        CriticalCheckTriageAssessed capturedEvent = (CriticalCheckTriageAssessed) eventStore.events().get(0);
        assertEquals("21522001:246454002=41847000", capturedEvent.getFoundChiefComplaintId());
        assertEquals(actualOutput, capturedEvent.getOutput());

        assertEquals(1, spyQueueSizeChangedSubscriber.getNumberOfHandleEventCalls());
    }

    @Test
    public void return_falsy_output_with_not_applicable_findings() {
        CriticalCheckAssesmentOutput actualOutput = new CheckForCriticalState(repository, queue)
                .execute(new CheckForCriticalStateRequest(
                        "21522001:246454002=41847000",
                        Arrays.asList("161891005", "418290006 |prurito (hallazgo)| : "
                                + "363698007 |sitio del hallazgo (atributo)| = 81745001")
                ));

        assertEquals(
                new CriticalCheckAssesmentOutput(false, null),
                actualOutput
        );

        assertEquals(actualOutput, queue.nextPending().getOutput());

        assertEquals(eventStore.events().size(), 1);
        CriticalCheckTriageAssessed capturedEvent = (CriticalCheckTriageAssessed) eventStore.events().get(0);
        assertEquals(capturedEvent.getFoundChiefComplaintId(), "21522001:246454002=41847000");
        assertEquals(capturedEvent.getOutput(), actualOutput);

        assertEquals(1, spyQueueSizeChangedSubscriber.getNumberOfHandleEventCalls());
    }

    @Test(expected = NoSuchElementException.class)
    public void return_truthy_output_with_applicable_critical_findings() {
        CriticalCheckAssesmentOutput actualOutput = new CheckForCriticalState(repository, queue)
                .execute(new CheckForCriticalStateRequest(
                        "21522001:246454002=41847000",
                        Collections.singletonList("90480005")
                ));

        assertEquals(
                new CriticalCheckAssesmentOutput(
                        true,
                        Arrays.asList(
                                "if unconscious place in the recovery position, if conscious try to reassure.",
                                "Provide Life Support Advice if required.",
                                "Take available analgesia for pain control.",
                                "Keep sample of vomit/stool if possible."
                        )
                ),
                actualOutput
        );

        assertEquals(0, spyQueueSizeChangedSubscriber.getNumberOfHandleEventCalls());

        queue.nextPending();
    }

}
