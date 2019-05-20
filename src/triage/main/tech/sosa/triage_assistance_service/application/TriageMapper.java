package tech.sosa.triage_assistance_service.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.snomed.languages.scg.SCGQueryBuilder;
import org.snomed.languages.scg.domain.model.Expression;
import tech.sosa.triage_assistance_service.application.dto.AlgorithmDTO;
import tech.sosa.triage_assistance_service.application.dto.AlgorithmLevelDTO;
import tech.sosa.triage_assistance_service.application.dto.ClinicalFindingDTO;
import tech.sosa.triage_assistance_service.application.dto.DiscriminatorDTO;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.domain.model.AlgorithmLevel;
import tech.sosa.triage_assistance_service.domain.model.AlgorithmLevelTitle;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.domain.model.ClinicalFindingTitle;
import tech.sosa.triage_assistance_service.domain.model.Discriminator;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageAlgorithm;

public class TriageMapper {

    private SCGQueryBuilder expressionBuilder;
    private List<RuntimeException> failures;

    public TriageMapper(SCGQueryBuilder expressionBuilder) {
        this.expressionBuilder = expressionBuilder;
        this.failures = new ArrayList<>();
    }

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
        validateTriage(triageDTO);

        return new Triage(
                parseChiefComplaint(triageDTO.chiefComplaint),
                parseTriageAlgorithm(triageDTO.algorithm)
        );
    }

    public ChiefComplaint buildChiefComplaint(String id, String title) {
        ClinicalFindingDTO chiefCompDTO = new ClinicalFindingDTO(id, title);

        RuntimeException e = validateChiefComplaint(chiefCompDTO);
        if (null != e) {
            throw e;
        }

        return new ChiefComplaint(
                new ClinicalFindingId(expressionBuilder.createQuery(id)),
                new ClinicalFindingTitle(title));
    }

    private ChiefComplaint parseChiefComplaint(ClinicalFindingDTO clinicalFindingDTO) {
        return new ChiefComplaint(
                new ClinicalFindingId(expressionBuilder.createQuery(clinicalFindingDTO.id)),
                new ClinicalFindingTitle(clinicalFindingDTO.title));
    }

    private void validateTriage(TriageDTO triageDTO) {
        failures = new ArrayList<>();
        failures.add(validateChiefComplaint(triageDTO.chiefComplaint));
        failures.addAll(validateAlgorithm(triageDTO.algorithm));

        failures = failures.stream().filter(Objects::nonNull).collect(Collectors.toList());

        if (!failures.isEmpty()) {
            RuntimeException r = new RuntimeException();
            failures.forEach(r::addSuppressed);
            throw r;
        }
    }

    private Collection<RuntimeException> validateAlgorithm(AlgorithmDTO algorithm) {
        AtomicInteger cIndex = new AtomicInteger();
        AtomicInteger iIndex = new AtomicInteger();

        Stream<RuntimeException> criticalStream = algorithm.criticalLevels.stream().flatMap(l ->
                validateLevel(l, new StringBuilder("#/algorithm/criticalLevels/")
                        .append(cIndex.getAndDecrement())).stream()
        );

        Stream<RuntimeException> intermediateStream = algorithm.intermediateLevels.stream()
                .flatMap(l ->
                        validateLevel(l, new StringBuilder("#/algorithm/intermediateLevels/")
                                .append(iIndex.getAndDecrement())).stream()
                );

        return concatStreams(
                criticalStream,
                intermediateStream
        ).collect(Collectors.toList());
    }

    private Collection<RuntimeException> validateLevel(
            AlgorithmLevelDTO algorithmLevelDTO, StringBuilder stringBuilder) {
        AtomicInteger index = new AtomicInteger();
        return algorithmLevelDTO.discriminators.stream()
                .map(d -> {
                    try {
                        expressionBuilder.createQuery(d.id);
                    } catch (Exception e) {
                        return new RuntimeException(
                                stringBuilder.append("/discriminators/").append(index.get())
                                        .append("/id").toString(),
                                e);
                    }
                    return null;
                }).collect(Collectors.toList());
    }

    private RuntimeException validateChiefComplaint(ClinicalFindingDTO chiefComplaint) {
        try {
            expressionBuilder.createQuery(chiefComplaint.id);
        } catch (Exception e) {
            return new RuntimeException("#/chiefComplaint/id", e);
        }
        return null;
    }

    public TriageDTO to(Triage triage) {
        return new TriageDTO(
                new ClinicalFindingDTO(
                        expressionToString(triage.chiefComplaint().id().value()),
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

    private AlgorithmLevelDTO mapAlgorithmLevel(AlgorithmLevel algorithmLevel) {
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
                expressionToString(discriminator.id().value()),
                discriminator.title().value(),
                discriminator.definition(),
                discriminator.questions()
        );
    }

    public String expressionToString(Expression chiefCompExp) {
        return String.join("+", chiefCompExp.getFocusConcepts())
                + Optional.ofNullable(chiefCompExp.getAttributes())
                .map(attrs -> ":" + attrs.stream().map(
                        a -> a.getAttributeId() + "=" + a.getAttributeValue().getConceptId()
                        ).collect(Collectors.joining(","))
                ).orElse("");
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
                new ClinicalFindingId(expressionBuilder.createQuery(discriminatorDTO.id)),
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
