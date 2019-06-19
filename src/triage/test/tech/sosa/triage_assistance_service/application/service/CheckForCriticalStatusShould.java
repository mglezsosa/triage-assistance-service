package tech.sosa.triage_assistance_service.application.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.snomed.languages.scg.SCGException;
import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.domain.model.CriticalCheckAssesmentOutput;
import tech.sosa.triage_assistance_service.domain.model.TriageDoesNotExistException;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;
import tech.sosa.triage_assistance_service.infrastructure.persistence.TriageRepositoryStub;
import tech.sosa.triage_assistance_service.utils.TestWithUtils;

public class CheckForCriticalStatusShould extends TestWithUtils {

    private TriageRepository repository;

    @Before
    public void setUp() throws IOException, URISyntaxException {
        TriageMapper mapper = new TriageMapper();
        repository = TriageRepositoryStub.with(mapper.from(readJSON(readFromResource("triageExample.json"),
                TriageDTO.class)));
    }

    @Test(expected = SCGException.class)
    public void throw_a_exception_if_triage_id_is_not_a_valid_snomed_expression() {
        new CheckForCriticalState(repository)
                .execute(new CheckForCriticalStateRequest(
                        "2152200112344455",
                        Collections.emptyList()
                ));
    }

    @Test(expected = TriageDoesNotExistException.class)
    public void throw_a_exception_if_selected_triage_does_not_exist() {
        new CheckForCriticalState(repository)
                .execute(new CheckForCriticalStateRequest(
                        "21522001",
                        Collections.emptyList()
                ));
    }

    @Test
    public void return_falsy_output_with_no_findings() {
        CriticalCheckAssesmentOutput actualOutput = new CheckForCriticalState(repository)
                .execute(new CheckForCriticalStateRequest(
                        "21522001:246454002=41847000",
                        Collections.emptyList()
                ));

        assertEquals(
                new CriticalCheckAssesmentOutput(false, null),
                actualOutput
        );
    }

    @Test
    public void return_falsy_output_with_not_applicable_findings() {
        CriticalCheckAssesmentOutput actualOutput = new CheckForCriticalState(repository)
                .execute(new CheckForCriticalStateRequest(
                        "21522001:246454002=41847000",
                        Arrays.asList("161891005", "418290006 |prurito (hallazgo)| : "
                                + "363698007 |sitio del hallazgo (atributo)| = 81745001")
                ));

        assertEquals(
                new CriticalCheckAssesmentOutput(false, null),
                actualOutput
        );
    }

    @Test
    public void return_truthy_output_with_applicable_critical_findings() {
        CriticalCheckAssesmentOutput actualOutput = new CheckForCriticalState(repository)
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
    }

}
