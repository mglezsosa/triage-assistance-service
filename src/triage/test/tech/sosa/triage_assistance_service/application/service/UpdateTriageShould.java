package tech.sosa.triage_assistance_service.application.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;
import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;
import tech.sosa.triage_assistance_service.infrastructure.persistence.TriageRepositoryStub;
import tech.sosa.triage_assistance_service.utils.TestWithUtils;

public class UpdateTriageShould extends TestWithUtils {

    private TriageRepository triageRepo;
    private TriageMapper triageMapper;
    private Triage updatableTriage;
    private Triage expectedTriage;
    private String updatedSource;

    @Before
    public void setUp() throws URISyntaxException, IOException {

        triageMapper = new TriageMapper();

        updatedSource = readFromResource("updatedTriageExample.json");
        String notUpdatedSource = readFromResource("triageExample.json");

        updatableTriage = triageMapper.from(readJSON(notUpdatedSource, TriageDTO.class));
        expectedTriage = triageMapper.from(readJSON(updatedSource, TriageDTO.class));

        // Avoid object reference to updatableTriage.
        triageRepo = TriageRepositoryStub
                .with(triageMapper.from(readJSON(notUpdatedSource, TriageDTO.class)));
    }

    @Test
    public void execute_correctly_with_valid_arguments() throws IOException {

        assertNotEquals(expectedTriage, triageRepo.find(updatableTriage.chiefComplaint()));

        new UpdateTriage(triageRepo, triageMapper).execute(
                new UpdateTriageRequest(readJSON(updatedSource, TriageDTO.class))
        );

        assertEquals(expectedTriage, triageRepo.find(updatableTriage.chiefComplaint()));
    }

}
