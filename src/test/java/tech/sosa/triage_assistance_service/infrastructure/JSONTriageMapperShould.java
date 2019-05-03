package tech.sosa.triage_assistance_service.infrastructure;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Objects;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaintId;
import tech.sosa.triage_assistance_service.domain.model.ClinicalFinding;
import tech.sosa.triage_assistance_service.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.domain.model.EmergencyLevel;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageAlgorithm;

public class JSONTriageMapperShould {

    @Test
    public void return_a_valid_triage_algorithm() {

        JSONObject obj = new JSONObject(
                new JSONTokener(
                        Objects.requireNonNull(getClass().getClassLoader()
                                .getResourceAsStream("triageExample.json"))
                )
        );

        Triage expectedTriage = new Triage(
                new ChiefComplaint(new ChiefComplaintId("SCTID:999")),
                new TriageAlgorithm(
                        new EmergencyLevel("1"),
                        Arrays.asList(
                                new ClinicalFinding(new ClinicalFindingId("SCTID:0")),
                                new ClinicalFinding(new ClinicalFindingId("SCTID:1")),
                                new ClinicalFinding(new ClinicalFindingId("SCTID:2"))
                        ),
                        Arrays.asList("Advice1"),
                        new TriageAlgorithm(
                                new EmergencyLevel("2"),
                                Arrays.asList(
                                        new ClinicalFinding(new ClinicalFindingId("SCTID:3")),
                                        new ClinicalFinding(new ClinicalFindingId("SCTID:4")),
                                        new ClinicalFinding(new ClinicalFindingId("SCTID:5"))
                                ),
                                null,
                                new TriageAlgorithm(
                                        new EmergencyLevel("3"),
                                        Arrays.asList(
                                                new ClinicalFinding(new ClinicalFindingId("SCTID:6")),
                                                new ClinicalFinding(new ClinicalFindingId("SCTID:7")),
                                                new ClinicalFinding(new ClinicalFindingId("SCTID:8"))
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

        Triage actualTriage = (new JSONTriageMapper()).from(obj);

        assertEquals(expectedTriage, actualTriage);
    }

}
