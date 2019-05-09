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
                new ChiefComplaint(new ChiefComplaintId("SCTID:999")),
                new TriageAlgorithm(
                        new EmergencyLevel("1"),
                        Arrays.asList(
                                new Discriminator(
                                        new ClinicalFindingId("SCTID:0"),
                                        Arrays.asList("Question1", "Question2"),
                                        "Definition1"),
                                new Discriminator(
                                        new ClinicalFindingId("SCTID:1"),
                                        Collections.singletonList("Question3"),
                                        "Definition2")
                        ),
                        Collections.singletonList("Advice1"),
                        new TriageAlgorithm(
                                new EmergencyLevel("2"),
                                Arrays.asList(
                                        new Discriminator(
                                                new ClinicalFindingId("SCTID:2"),
                                                Arrays.asList("Question4", "Question5"),
                                                "Definition3"),
                                        new Discriminator(
                                                new ClinicalFindingId("SCTID:3"),
                                                Collections.singletonList("Question6"),
                                                "Definition4")
                                ),
                                null,
                                new TriageAlgorithm(
                                        new EmergencyLevel("3"),
                                        Arrays.asList(
                                                new Discriminator(
                                                        new ClinicalFindingId("SCTID:4"),
                                                        Arrays.asList("Question7", "Question8"),
                                                        "Definition5"),
                                                new Discriminator(
                                                        new ClinicalFindingId("SCTID:5"),
                                                        Collections.singletonList("Question9"),
                                                        "Definition6")
                                        ),
                                        null,
                                        new TriageAlgorithm(
                                                new EmergencyLevel("4"),
                                                null,
                                                Arrays.asList("Advice2", "Advice3"),
                                                null
                                        )
                                )
                        )
                )
        );
    }

    @Test
    public void assess_correctly_with_a_collection_of_clinical_findings() {

        Collection<ClinicalFinding> patientFindings = Arrays.asList(
                new ClinicalFinding(new ClinicalFindingId("SCTID:2")),
                new ClinicalFinding(new ClinicalFindingId("SCTID:5"))
        );

        TriageOutput expectedTriage = new TriageOutput(
                new EmergencyLevel("2"),
                Collections.emptyList()
        );

        assertEquals(expectedTriage, aTriage.assess(patientFindings));
    }
}
