package tech.sosa.triage_assistance_service.application.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;
import tech.sosa.triage_assistance_service.infrastructure.TriageRepositoryStub;
import tech.sosa.triage_assistance_service.utils.TestWithUtils;

public class ViewAllTriagesShould extends TestWithUtils {

    private TriageRepository triageRepo;
    private TriageMapper triageMapper;
    private TriageDTO triage1;
    private TriageDTO triage2;

    @Before
    public void setUp() throws URISyntaxException, IOException {

        triageMapper = new TriageMapper();

        // Triages below are identical except for the id.
        triage1 = readJSON(readFromResource("triageExample.json"), TriageDTO.class);
        triage2 = readJSON(readFromResource("triageExample2.json"), TriageDTO.class);

        triageRepo = TriageRepositoryStub.with(
                triageMapper.from(triage1),
                triageMapper.from(triage2)
        );
    }

    @Test
    public void return_a_dto_collection_of_all_existing_triages() {
        Collection<TriageDTO> output = new ViewAllTriages(triageRepo, triageMapper).execute();
        assertTrue(equalsIgnoreOrder(output, Arrays.asList(triage2, triage1)));
    }

}
