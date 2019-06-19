package tech.sosa.triage_assistance_service.domain.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import org.junit.Before;
import org.junit.Test;
import tech.sosa.triage_assistance_service.domain.event.AuditingCriticalCheckTriageAssessedSubscriber;
import tech.sosa.triage_assistance_service.domain.event.NonCriticalAssessingEqueuerSubscriber;
import tech.sosa.triage_assistance_service.port.adapter.InMemoryPendingTriagesQueue;
import tech.sosa.triage_assistance_service.port.adapter.LoggingEventStore;
import tech.sosa.triage_assistance_service.util.event.EventPublisher;
import tech.sosa.triage_assistance_service.utils.TestWithUtils;


public class TriageShould extends TestWithUtils {

    private Triage aTriage;
    private AlgorithmLevel ftfNow;
    private AlgorithmLevel ftfSoon;
    private AlgorithmLevel adviceOnly;
    private PendingTriagesQueue queue;

    @Before
    public void setUp() {
        ftfNow = new AlgorithmLevel(
                new AlgorithmLevelTitle("FtF Now"),
                Arrays.asList(
                        new Discriminator(
                                new ClinicalFindingId("90480005"),
                                new ClinicalFindingTitle(
                                        "Vía respiratoria comprometida"),
                                Arrays.asList("Are they awake?",
                                        "Are they struggling to breathe?",
                                        "Can they get their breath in?",
                                        "Do they make a gurgling sound when they breathe?"
                                ),
                                "An airway may be compromised either because it cannot be kept open or because the airway protective reflexes (that stop inhalation) have been lost. Failure to keep the airway open will result either in intermittent total obstruction or in partial obstruction. This will manifest itself as snoring or bubbling sounds during breathing."),
                        new Discriminator(
                                new ClinicalFindingId("300361008"),
                                new ClinicalFindingTitle("Vómito con sangre"),
                                Collections.emptyList(),
                                "Vomited blood may be fresh (bright or dark red) or coffee ground in appearance.")
                ),
                Arrays.asList(
                        "if unconscious place in the recovery position, if conscious try to reassure.",
                        "Provide Life Support Advice if required.",
                        "Take available analgesia for pain control.",
                        "Keep sample of vomit/stool if possible."
                ),
                true
        );

        ftfSoon = new AlgorithmLevel(
                new AlgorithmLevelTitle("FtF Soon"),
                Collections.singletonList(
                        new Discriminator(
                                new ClinicalFindingId("267055007"),
                                new ClinicalFindingTitle("Heces negras"),
                                Collections.emptyList(),
                                "Something")
                ),
                Arrays.asList(
                        "Take available analgesia for pain control.",
                        "Call back if symptoms worsen or concerned.",
                        "Keep sample of stool if possible."
                ),
                false
        );

        adviceOnly = new AlgorithmLevel(
                new AlgorithmLevelTitle("Advice only"),
                Arrays.asList(
                        "Maintain uid intake – plenty of clear uids/consider oral rehydration therapy.",
                        "Paracetamol qds for pain and temperature control.",
                        "Rest.",
                        "Refer to GP if symptoms persist.",
                        "Call back if symptoms worsen or concerned."
                )
        );

        aTriage = new Triage(
                new ChiefComplaint(
                        new ClinicalFindingId("21522001:246454002=41847000"),
                        new ClinicalFindingTitle("Dolor abdominal en adultos")),
                new TriageAlgorithm(Arrays.asList(
                        ftfNow,
                        ftfSoon,
                        adviceOnly
                ))
        );

        EventPublisher.instance().reset();

        EventPublisher.instance().subscribe(new AuditingCriticalCheckTriageAssessedSubscriber(
                new LoggingEventStore(objectMapper)
        ));

        queue = new InMemoryPendingTriagesQueue(new ArrayList<>());

        EventPublisher.instance().subscribe(new NonCriticalAssessingEqueuerSubscriber(queue));
    }

    @Test(expected = NoSuchElementException.class)
    public void return_FtFNow_level_on_full_assessment() {

        Collection<ClinicalFinding> mostCriticalFinding = Collections.singletonList(
                new ClinicalFinding(new ClinicalFindingId("90480005"), null)
        );

        assertEquals(ftfNow, aTriage.fullyAssess(mostCriticalFinding));

        queue.nextPending();
    }

    @Test(expected = NoSuchElementException.class)
    public void return_FtFSoon_level_on_full_assessment() {

        Collection<ClinicalFinding> mostCriticalFinding = Collections.singletonList(
                new ClinicalFinding(new ClinicalFindingId("267055007"), null)
        );

        assertEquals(ftfSoon, aTriage.fullyAssess(mostCriticalFinding));

        queue.nextPending();
    }

    @Test(expected = NoSuchElementException.class)
    public void return_AdviceOnly_level_on_full_assessment() {

        Collection<ClinicalFinding> noFindings = Collections.emptyList();
        Collection<ClinicalFinding> notApplicableFindings = Arrays.asList(
                new ClinicalFinding(new ClinicalFindingId("161891005"), null),
                new ClinicalFinding(new ClinicalFindingId(
                        "418290006 |prurito (hallazgo)| : "
                                + "363698007 |sitio del hallazgo (atributo)| = 81745001"), null)
        );

        assertEquals(adviceOnly, aTriage.fullyAssess(noFindings));
        assertEquals(adviceOnly, aTriage.fullyAssess(notApplicableFindings));

        queue.nextPending();
    }

    @Test
    public void return_falsy_on_critical_state_checking() {
        Collection<ClinicalFinding> noFindings = Collections.emptyList();

        Collection<ClinicalFinding> notApplicableFindings = Arrays.asList(
                new ClinicalFinding(new ClinicalFindingId("161891005"), null),
                new ClinicalFinding(new ClinicalFindingId(
                        "418290006 |prurito (hallazgo)| : "
                                + "363698007 |sitio del hallazgo (atributo)| = 81745001"), null)
        );

        Collection<ClinicalFinding> mostCriticalFinding = Collections.singletonList(
                new ClinicalFinding(new ClinicalFindingId("267055007"), null)
        );

        assertEquals(
                new CriticalCheckAssesmentOutput(false, null),
                aTriage.checkForCriticalState(mostCriticalFinding));

        assertEquals(
                new CriticalCheckAssesmentOutput(false, null),
                queue.nextPending().getOutput()
        );

        assertEquals(
                new CriticalCheckAssesmentOutput(false, null),
                aTriage.checkForCriticalState(noFindings));

        assertEquals(
                new CriticalCheckAssesmentOutput(false, null),
                queue.nextPending().getOutput()
        );

        assertEquals(
                new CriticalCheckAssesmentOutput(false, null),
                aTriage.checkForCriticalState(notApplicableFindings));

        assertEquals(
                new CriticalCheckAssesmentOutput(false, null),
                queue.nextPending().getOutput()
        );
    }

    @Test(expected = NoSuchElementException.class)
    public void return_truthy_on_critical_state_checking() {
        Collection<ClinicalFinding> mostCriticalFinding = Collections.singletonList(
                new ClinicalFinding(new ClinicalFindingId("90480005"), null)
        );

        CriticalCheckAssesmentOutput output = aTriage.checkForCriticalState(mostCriticalFinding);

        assertEquals(
                new CriticalCheckAssesmentOutput(true, ftfNow.advices()),
                output
        );

        queue.nextPending();
    }
}
