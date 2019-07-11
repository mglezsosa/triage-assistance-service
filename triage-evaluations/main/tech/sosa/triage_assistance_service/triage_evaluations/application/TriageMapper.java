package tech.sosa.triage_assistance_service.triage_evaluations.application;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import tech.sosa.triage_assistance_service.shared.application.dto.AlgorithmDTO;
import tech.sosa.triage_assistance_service.shared.application.dto.AlgorithmLevelDTO;
import tech.sosa.triage_assistance_service.shared.application.dto.ClinicalFindingDTO;
import tech.sosa.triage_assistance_service.shared.application.dto.DiscriminatorDTO;
import tech.sosa.triage_assistance_service.shared.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.AlgorithmLevel;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.AlgorithmLevelTitle;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFindingTitle;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.Discriminator;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.Triage;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageAlgorithm;

public class TriageMapper {

    @SafeVarargs
    private static <E> Stream<E> concatStreams(Stream<E>... streams) {
        if (streams.length == 1) {
            return streams[0];
        }
        return Stream.concat(
                streams[0],
                concatStreams(Arrays.copyOfRange(streams, 1, streams.length))
        );
    }

    public Triage from(TriageDTO triageDTO) {
        return new Triage(
                parseChiefComplaint(triageDTO.chiefComplaint),
                parseTriageAlgorithm(triageDTO.algorithm)
        );
    }

    public ChiefComplaint buildChiefComplaint(String id, String title) {
        return new ChiefComplaint(
                new ClinicalFindingId(id),
                new ClinicalFindingTitle(title));
    }

    private ChiefComplaint parseChiefComplaint(ClinicalFindingDTO clinicalFindingDTO) {
        return buildChiefComplaint(clinicalFindingDTO.id, clinicalFindingDTO.title);
    }

    public TriageDTO to(Triage triage) {
        return new TriageDTO(
                new ClinicalFindingDTO(
                        triage.chiefComplaint().id().value(),
                        triage.chiefComplaint().title().value()
                ),
                mapTriageAlgorithm(triage.algorithm())
        );
    }

    private AlgorithmDTO mapTriageAlgorithm(TriageAlgorithm algorithm) {
        return new AlgorithmDTO(
                filterAndMapCriticalLevels(algorithm),
                filterAndMapIntermediateLevels(algorithm),
                findAndMapDefaultLevel(algorithm)
        );
    }

    private AlgorithmLevelDTO findAndMapDefaultLevel(TriageAlgorithm algorithm) {
        return mapAlgorithmLevel(algorithm.levels().get(algorithm.levels().size() - 1));
    }

    private List<AlgorithmLevelDTO> filterAndMapIntermediateLevels(TriageAlgorithm algorithm) {
        return algorithm.levels().stream()
                .filter(l -> !l.isCritical())
                .takeWhile(l -> l.discriminators() != null)
                .map(this::mapAlgorithmLevel)
                .collect(Collectors.toList());
    }

    private List<AlgorithmLevelDTO> filterAndMapCriticalLevels(TriageAlgorithm algorithm) {
        return algorithm.levels().stream()
                .filter(AlgorithmLevel::isCritical)
                .map(this::mapAlgorithmLevel)
                .collect(Collectors.toList());
    }

    public AlgorithmLevelDTO mapAlgorithmLevel(AlgorithmLevel algorithmLevel) {
        return new AlgorithmLevelDTO(
                algorithmLevel.title().value(),
                algorithmLevel.advices(),
                Optional.ofNullable(algorithmLevel.discriminators())
                        .map(ds -> ds.stream().map(this::mapDiscriminator)
                                .collect(Collectors.toList()))
                        .orElse(null)
        );
    }

    private DiscriminatorDTO mapDiscriminator(Discriminator discriminator) {
        return new DiscriminatorDTO(
                discriminator.id().value(),
                discriminator.title().value(),
                discriminator.definition(),
                discriminator.questions()
        );
    }

    private TriageAlgorithm parseTriageAlgorithm(AlgorithmDTO algorithmDTO) {

        Stream<AlgorithmLevel> criticalLevels = algorithmDTO.criticalLevels.stream()
                .map(this::parseCriticalLevel);

        Stream<AlgorithmLevel> intermediateLevels = algorithmDTO.intermediateLevels.stream()
                .map(this::parseNonCriticalLevel);

        Stream<AlgorithmLevel> defaultLevel = Stream
                .of(parseDefaultLevel(algorithmDTO.defaultLevel));

        return new TriageAlgorithm(
                concatStreams(criticalLevels, intermediateLevels, defaultLevel)
                        .collect(Collectors.toList())
        );
    }

    private Collection<Discriminator> parseDiscriminators(
            Collection<DiscriminatorDTO> discriminatorDTOS) {
        return discriminatorDTOS.stream().map(this::parseDiscriminator)
                .collect(Collectors.toList());
    }

    private Discriminator parseDiscriminator(DiscriminatorDTO discriminatorDTO) {
        return new Discriminator(
                new ClinicalFindingId(discriminatorDTO.id),
                new ClinicalFindingTitle(discriminatorDTO.title),
                discriminatorDTO.questions,
                discriminatorDTO.definition
        );
    }

    private AlgorithmLevel parseAlgorithmLevel(AlgorithmLevelDTO algorithmLevelDTO,
            boolean isCritical) {
        return new AlgorithmLevel(
                new AlgorithmLevelTitle(algorithmLevelDTO.levelTitle),
                parseDiscriminators(algorithmLevelDTO.discriminators),
                algorithmLevelDTO.advices,
                isCritical);
    }

    private AlgorithmLevel parseCriticalLevel(AlgorithmLevelDTO algorithmLevelDTO) {
        return parseAlgorithmLevel(algorithmLevelDTO, true);
    }

    private AlgorithmLevel parseNonCriticalLevel(AlgorithmLevelDTO algorithmLevelDTO) {
        return parseAlgorithmLevel(algorithmLevelDTO, false);
    }

    private AlgorithmLevel parseDefaultLevel(AlgorithmLevelDTO defaultLevelDTO) {
        return new AlgorithmLevel(
                new AlgorithmLevelTitle(defaultLevelDTO.levelTitle),
                defaultLevelDTO.advices
        );
    }
}
