package tech.sosa.triage_assistance_service.application;

import java.util.Collection;
import java.util.stream.Collectors;
import tech.sosa.triage_assistance_service.application.dto.DiscriminatorDTO;
import tech.sosa.triage_assistance_service.application.dto.TriageAlgorithmDTO;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaintId;
import tech.sosa.triage_assistance_service.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.domain.model.Discriminator;
import tech.sosa.triage_assistance_service.domain.model.EmergencyLevel;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageAlgorithm;

public class TriageMapper {

    public Triage from(TriageDTO triageDTO) {
        return new Triage(
                new ChiefComplaint(new ChiefComplaintId(triageDTO.chiefComplaint())),
                parseTriageAlgorithm(triageDTO.algorithm())
        );
    }

    public TriageDTO to(Triage triage) {
        TriageDTO triageDTO = new TriageDTO();

        triageDTO.chiefComplaint(mapChiefComplaint(triage.chiefComplaint()));
        triageDTO.algorithm(mapTriageAlgorithm(triage.algorithm()));

        return triageDTO;
    }

    private String mapChiefComplaint(ChiefComplaint chiefComplaint) {
        return chiefComplaint.id().toString();
    }

    private TriageAlgorithm parseTriageAlgorithm(TriageAlgorithmDTO triageAlgorithmDTO) {

        EmergencyLevel emLevel = new EmergencyLevel(triageAlgorithmDTO.emergencyLevel());

        Collection<String> advices = triageAlgorithmDTO.advices().orElse(null);

        Collection<Discriminator> discriminators = triageAlgorithmDTO.discriminators().map(
                d -> d.stream().map(this::parseDiscriminator).collect(Collectors.toList())
        ).orElse(null);

        TriageAlgorithm lowerLevelAlg = triageAlgorithmDTO.lowerLevel()
                .map(this::parseTriageAlgorithm)
                .orElse(null);

        return new TriageAlgorithm(emLevel, discriminators, advices, lowerLevelAlg);
    }

    private Discriminator parseDiscriminator(DiscriminatorDTO discriminatorDTO) {
        return new Discriminator(
                new ClinicalFindingId(discriminatorDTO.id()),
                discriminatorDTO.questions(),
                discriminatorDTO.definition());
    }

    private TriageAlgorithmDTO mapTriageAlgorithm(TriageAlgorithm algorithm) {
        return new TriageAlgorithmDTO(
                algorithm.emergencyLevel().value(),
                algorithm.discriminators()
                        .map(ds -> ds.stream().map(this::mapDiscriminator)
                                .collect(Collectors.toList()))
                        .orElse(null),
                algorithm.advices().orElse(null),
                algorithm.lowerLevelAlgorithm()
                        .map(this::mapTriageAlgorithm)
                        .orElse(null)
        );
    }

    private DiscriminatorDTO mapDiscriminator(Discriminator discriminator) {
        return new DiscriminatorDTO(
                discriminator.id().toString(),
                discriminator.questions(),
                discriminator.definition());
    }
}
