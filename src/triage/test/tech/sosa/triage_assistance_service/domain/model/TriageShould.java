package tech.sosa.triage_assistance_service.domain.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;


public class TriageShould {

    private Triage aTriage;

    @Before
    public void setUp() {
        aTriage = new Triage(
                new ChiefComplaint(
                        new ClinicalFindingId("21522001:246454002=41847000"),
                        new ClinicalFindingTitle("Dolor abdominal en adultos")),
                new TriageAlgorithm(Arrays.asList(
                        new AlgorithmLevel(
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
                        ),
                        new AlgorithmLevel(
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
                        ),
                        new AlgorithmLevel(
                                new AlgorithmLevelTitle("Advice only"),
                                Arrays.asList(
                                        "Maintain uid intake – plenty of clear uids/consider oral rehydration therapy.",
                                        "Paracetamol qds for pain and temperature control.",
                                        "Rest.",
                                        "Refer to GP if symptoms persist.",
                                        "Call back if symptoms worsen or concerned."
                                )
                        )
                ))
        );
    }

    @Test
    public void return_FtFNow_level() {

        Collection<ClinicalFinding> mostCriticalFinding = Collections.singletonList(
                new ClinicalFinding(new ClinicalFindingId("90480005"), null)
        );

        TriageOutput expectedTriage = new TriageOutput(
                new AlgorithmLevelTitle("FtF Now"),
                Arrays.asList(
                        "if unconscious place in the recovery position, if conscious try to reassure.",
                        "Provide Life Support Advice if required.",
                        "Take available analgesia for pain control.",
                        "Keep sample of vomit/stool if possible."
                )
        );

        assertEquals(expectedTriage, aTriage.assess(mostCriticalFinding));
    }

    @Test
    public void return_FtFSoon_level() {

        Collection<ClinicalFinding> mostCriticalFinding = Collections.singletonList(
                new ClinicalFinding(new ClinicalFindingId("267055007"), null)
        );

        TriageOutput expectedTriage = new TriageOutput(
                new AlgorithmLevelTitle("FtF Soon"),
                Arrays.asList(
                        "Take available analgesia for pain control.",
                        "Call back if symptoms worsen or concerned.",
                        "Keep sample of stool if possible."
                )
        );

        assertEquals(expectedTriage, aTriage.assess(mostCriticalFinding));
    }

    @Test
    public void return_AdviceOnly_level() {

        Collection<ClinicalFinding> noFindings = Collections.emptyList();
        Collection<ClinicalFinding> notApplicableFindings = Arrays.asList(
                new ClinicalFinding(new ClinicalFindingId("161891005"), null),
                new ClinicalFinding(new ClinicalFindingId(
                        "418290006 |prurito (hallazgo)| : "
                                + "363698007 |sitio del hallazgo (atributo)| = 81745001"), null)
        );

        TriageOutput expectedTriage = new TriageOutput(
                new AlgorithmLevelTitle("Advice only"),
                Arrays.asList(
                        "Maintain uid intake – plenty of clear uids/consider oral rehydration therapy.",
                        "Paracetamol qds for pain and temperature control.",
                        "Rest.",
                        "Refer to GP if symptoms persist.",
                        "Call back if symptoms worsen or concerned."
                )
        );

        assertEquals(expectedTriage, aTriage.assess(noFindings));
        assertEquals(expectedTriage, aTriage.assess(notApplicableFindings));
    }
}
