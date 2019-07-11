package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.shared.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;
import tech.sosa.triage_assistance_service.triage_evaluations.persistence.TriageRepositoryStub;
import tech.sosa.triage_assistance_service.triage_evaluations.utils.TestWithUtils;

public class CreateTriageShould extends TestWithUtils {

    private TriageRepository triageRepo;
    private TriageMapper triageMapper;

    @Before
    public void setUp() {
        triageRepo = TriageRepositoryStub.empty();
        triageMapper = new TriageMapper();
    }

    @Test
    public void execute_correctly_with_valid_arguments() throws IOException, URISyntaxException {

        assertNull(triageRepo.find(new ChiefComplaint(
                new ClinicalFindingId("21522001:246454002=41847000"), null)));

        new CreateTriage(triageRepo, triageMapper).execute(
                new CreateTriageRequest(
                        readJSON(readFromResource("triageExample.json"), TriageDTO.class))
        );

        assertNotNull(triageRepo.find(new ChiefComplaint(
                new ClinicalFindingId("21522001:246454002=41847000"), null)));
    }

}
