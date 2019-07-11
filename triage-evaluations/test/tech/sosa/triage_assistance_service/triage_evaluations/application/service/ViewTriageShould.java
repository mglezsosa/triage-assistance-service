package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.shared.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.Triage;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;
import tech.sosa.triage_assistance_service.triage_evaluations.persistence.TriageRepositoryStub;
import tech.sosa.triage_assistance_service.triage_evaluations.utils.TestWithUtils;

public class ViewTriageShould extends TestWithUtils {

    private TriageRepository triageRepo;
    private TriageMapper triageMapper;
    private Triage requestedTriage;
    private String source;

    @Before
    public void setUp() throws URISyntaxException, IOException {

        triageMapper = new TriageMapper();

        source = readFromResource("triageExample.json");

        requestedTriage = triageMapper.from(readJSON(source, TriageDTO.class));

        triageRepo = TriageRepositoryStub.with(requestedTriage);
    }

    @Test
    public void execute_correctly_with_valid_arguments() throws IOException {

        TriageDTO output = new ViewTriage(triageRepo, triageMapper).execute(
                new ViewTriageRequest(requestedTriage.chiefComplaint().id().value())
        );

        assertEquals(
                readJSON(source, TriageDTO.class),
                output
        );
    }

}
