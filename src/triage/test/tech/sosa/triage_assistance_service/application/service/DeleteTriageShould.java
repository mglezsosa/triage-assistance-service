package tech.sosa.triage_assistance_service.application.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;
import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;
import tech.sosa.triage_assistance_service.infrastructure.TriageRepositoryStub;
import tech.sosa.triage_assistance_service.utils.TestWithUtils;

public class DeleteTriageShould extends TestWithUtils {

    private TriageRepository repo;
    private TriageMapper mapper;
    private Triage existingTriage;

    @Before
    public void setUp() throws IOException, URISyntaxException {
        mapper = new TriageMapper();
        existingTriage = mapper.from(readJSON(
                readFromResource("triageExample.json"),
                TriageDTO.class
        ));
        repo = TriageRepositoryStub.with(
                mapper.from(readJSON(
                        readFromResource("triageExample.json"),
                        TriageDTO.class
                ))
        );
    }

    @Test
    public void delete_correctly_an_existing_triage() {

        assertNotNull(repo.find(existingTriage.chiefComplaint()));

        new DeleteTriage(repo, mapper).execute(
                new DeleteTriageRequest(existingTriage.chiefComplaint().id().value())
        );

        assertNull(repo.find(existingTriage.chiefComplaint()));
    }

}
