package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;
import org.snomed.languages.scg.SCGException;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.shared.application.dto.AlgorithmLevelDTO;
import tech.sosa.triage_assistance_service.shared.application.dto.DiscriminatorDTO;
import tech.sosa.triage_assistance_service.shared.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.AuditingFullTriageAssessmentSubscriber;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFindingTitle;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.CriticalCheckAssesmentOutput;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageDoesNotExistException;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;
import tech.sosa.triage_assistance_service.triage_evaluations.persistence.TriageRepositoryStub;
import tech.sosa.triage_assistance_service.triage_evaluations.port.adapter.InMemoryPendingTriagesQueue;
import tech.sosa.triage_assistance_service.identity_access.port.adapter.LoggingEventStore;
import tech.sosa.triage_assistance_service.shared.domain.event.EventPublisher;
import tech.sosa.triage_assistance_service.triage_evaluations.utils.TestWithUtils;

public class FullyEvaluateShould extends TestWithUtils {

    private PendingTriagesQueue queue;
    private TriageRepository repository;
    private TriageMapper mapper;

    @Before
    public void setUp() throws IOException, URISyntaxException {
        mapper = new TriageMapper();
        repository = TriageRepositoryStub.with(mapper.from(readJSON(readFromResource("triageExample.json"),
                TriageDTO.class)));
        queue = new InMemoryPendingTriagesQueue(new ArrayList<>());

        EventPublisher.instance().reset();
        EventPublisher.instance().subscribe(new AuditingFullTriageAssessmentSubscriber(
                new LoggingEventStore(objectMapper)));
    }

    @Test(expected = SCGException.class)
    public void throw_a_exception_if_triage_id_is_not_a_valid_snomed_expression() {
        new FullyEvaluate(repository, mapper, queue)
                .execute(new FullyEvaluateRequest(
                        "2152200112344455",
                        Collections.emptyList()
                ));
    }

    @Test(expected = TriageDoesNotExistException.class)
    public void throw_a_exception_if_selected_triage_does_not_exist() {
        new FullyEvaluate(repository, mapper, queue)
                .execute(new FullyEvaluateRequest(
                        "21522001",
                        Collections.emptyList()
                ));
    }

    @Test
    public void return_AdviceOnly_output_with_no_findings() {
        AlgorithmLevelDTO actualOutput = new FullyEvaluate(repository, mapper, queue)
                .execute(new FullyEvaluateRequest(
                        "21522001:246454002=41847000",
                        Collections.emptyList()
                ));

        assertEquals(
                new AlgorithmLevelDTO("Advice only",
                        Arrays.asList(
                                "Maintain uid intake – plenty of clear uids/consider oral rehydration therapy.",
                                "Paracetamol qds for pain and temperature control.",
                                "Rest.",
                                "Refer to GP if symptoms persist.",
                                "Call back if symptoms worsen or concerned."
                        ),
                        null
                ),
                actualOutput
        );
    }

    @Test
    public void return_AdviceOnly_output_with_not_applicable_findings() {
        AlgorithmLevelDTO actualOutput = new FullyEvaluate(repository, mapper, queue)
                .execute(new FullyEvaluateRequest(
                        "21522001:246454002=41847000",
                        Arrays.asList("161891005", "418290006 |prurito (hallazgo)| : "
                                + "363698007 |sitio del hallazgo (atributo)| = 81745001")
                ));

        assertEquals(
                new AlgorithmLevelDTO("Advice only",
                        Arrays.asList(
                                "Maintain uid intake – plenty of clear uids/consider oral rehydration therapy.",
                                "Paracetamol qds for pain and temperature control.",
                                "Rest.",
                                "Refer to GP if symptoms persist.",
                                "Call back if symptoms worsen or concerned."
                        ),
                        null
                ),
                actualOutput
        );
    }

    @Test
    public void return_FtFNow_output_with_applicable_critical_findings() {
        AlgorithmLevelDTO actualOutput = new FullyEvaluate(repository, mapper, queue)
                .execute(new FullyEvaluateRequest(
                        "21522001:246454002=41847000",
                        Collections.singletonList("90480005")
                ));

        assertEquals(
                new AlgorithmLevelDTO(
                        "FtF Now",
                        Arrays.asList(
                                "if unconscious place in the recovery position, if conscious try to reassure.",
                                "Provide Life Support Advice if required.",
                                "Take available analgesia for pain control.",
                                "Keep sample of vomit/stool if possible."
                        ),
                        Arrays.asList(
                                new DiscriminatorDTO(
                                        "90480005",
                                        "Vía respiratoria comprometida",
                                        "An airway may be compromised either because it cannot be kept open or because the airway protective reflexes (that stop inhalation) have been lost. Failure to keep the airway open will result either in intermittent total obstruction or in partial obstruction. This will manifest itself as snoring or bubbling sounds during breathing.",
                                        Arrays.asList("Are they awake?",
                                                "Are they struggling to breathe?",
                                                "Can they get their breath in?",
                                                "Do they make a gurgling sound when they breathe?"
                                        )
                                ),
                                new DiscriminatorDTO(
                                        "300361008",
                                        "Vómito con sangre",
                                        "Vomited blood may be fresh (bright or dark red) or coffee ground in appearance.",
                                        Collections.emptyList()
                                )
                        )
                ),
                actualOutput
        );
    }

    @Test
    public void return_FtFSoon_output_with_applicable_critical_findings() {
        AlgorithmLevelDTO actualOutput = new FullyEvaluate(repository, mapper, queue)
                .execute(new FullyEvaluateRequest(
                        "21522001:246454002=41847000",
                        Collections.singletonList("267055007")
                ));

        assertEquals(
                new AlgorithmLevelDTO(
                        "FtF Soon",
                        Arrays.asList(
                                "Take available analgesia for pain control.",
                                "Call back if symptoms worsen or concerned.",
                                "Keep sample of stool if possible."
                        ),
                        Collections.singletonList(
                                new DiscriminatorDTO(
                                        "267055007",
                                        "Heces negras",
                                        "Something",
                                        Collections.emptyList()
                                )
                        )
                ),
                actualOutput
        );
    }

    @Test
    public void return_FtFSoon_output_with_applicable_critical_findings_completing_an_existing_in_process_assessment() {

        queue.enqueue(new CriticalCheckTriageAssessed("1234",
                        new ChiefComplaint(
                                new ClinicalFindingId("21522001:246454002=41847000"),
                                new ClinicalFindingTitle("Dolor abdominal en adultos")),
                        new CriticalCheckAssesmentOutput(false, null)
                )
        );

        CriticalCheckTriageAssessed prevAssessment = queue.nextPending();

        assertEquals(prevAssessment.getId(), "1234");

        AlgorithmLevelDTO actualOutput = new FullyEvaluate(repository, mapper, queue)
                .execute(new FullyEvaluateRequest(
                        "21522001:246454002=41847000",
                        Collections.singletonList("267055007"),
                        "1234"
                ));

        assertEquals(
                new AlgorithmLevelDTO(
                        "FtF Soon",
                        Arrays.asList(
                                "Take available analgesia for pain control.",
                                "Call back if symptoms worsen or concerned.",
                                "Keep sample of stool if possible."
                        ),
                        Collections.singletonList(
                                new DiscriminatorDTO(
                                        "267055007",
                                        "Heces negras",
                                        "Something",
                                        Collections.emptyList()
                                )
                        )
                ),
                actualOutput
        );
    }

    @Test(expected = NoSuchElementException.class)
    public void return_FtFSoon_output_with_applicable_critical_findings_completing_an_inexisting_in_process_assessment() {

        AlgorithmLevelDTO actualOutput = new FullyEvaluate(repository, mapper, queue)
                .execute(new FullyEvaluateRequest(
                        "21522001:246454002=41847000",
                        Collections.singletonList("267055007"),
                        "1234" // inexisting
                ));

        assertEquals(
                new AlgorithmLevelDTO(
                        "FtF Soon",
                        Arrays.asList(
                                "Take available analgesia for pain control.",
                                "Call back if symptoms worsen or concerned.",
                                "Keep sample of stool if possible."
                        ),
                        Collections.singletonList(
                                new DiscriminatorDTO(
                                        "267055007",
                                        "Heces negras",
                                        "Something",
                                        Collections.emptyList()
                                )
                        )
                ),
                actualOutput
        );
    }

}
