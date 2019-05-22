package tech.sosa.triage_assistance_service.application;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.snomed.languages.scg.SCGException;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.domain.model.AlgorithmLevel;
import tech.sosa.triage_assistance_service.domain.model.AlgorithmLevelTitle;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.domain.model.ClinicalFindingTitle;
import tech.sosa.triage_assistance_service.domain.model.Discriminator;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageAlgorithm;
import tech.sosa.triage_assistance_service.utils.TestWithUtils;

public class TriageMapperShould extends TestWithUtils {

    private Triage aTriageObject;
    private TriageDTO aTriageDTO;
    private TriageMapper mapper;

    @Before
    public void setUp() throws IOException, URISyntaxException {
        mapper = new TriageMapper();
        aTriageObject = new Triage(
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
        aTriageDTO = readJSON(readFromResource("triageExample.json"), TriageDTO.class);
    }

    @Test
    public void map_a_triageDTO_to_a_correct_triage_object() {
        Triage expectedTriage = aTriageObject;
        Triage actualTriage = mapper.from(aTriageDTO);

        assertEquals(expectedTriage, actualTriage);
    }

    @Test(expected = SCGException.class)
    public void throw_a_composite_exception_validating_a_triageDTO_with_invalid_SNOMED_expressions() {
        aTriageDTO.chiefComplaint.id = "5435234";
        new ArrayList<>(aTriageDTO.algorithm.criticalLevels.get(0).discriminators)
                .get(0).id = "918389598";

        mapper.from(aTriageDTO);
    }

    @Test
    public void map_a_triage_object_to_a_triageDTO() {
        TriageDTO expectedTriageDTO = aTriageDTO;
        TriageDTO actualTriageDTO = mapper.to(aTriageObject);

        assertEquals(expectedTriageDTO, actualTriageDTO);
    }
}
