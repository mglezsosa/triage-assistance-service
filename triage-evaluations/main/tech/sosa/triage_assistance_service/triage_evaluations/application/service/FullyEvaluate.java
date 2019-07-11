package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import java.util.stream.Collectors;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.shared.application.dto.AlgorithmLevelDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.AlgorithmLevel;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFinding;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.Triage;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageDoesNotExistException;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;

public class FullyEvaluate implements ApplicationService<AlgorithmLevelDTO, FullyEvaluateRequest> {

    private final TriageRepository repository;
    private final TriageMapper mapper;
    private final PendingTriagesQueue queue;

    public FullyEvaluate(TriageRepository repository, TriageMapper mapper, PendingTriagesQueue queue) {
        this.repository = repository;
        this.mapper = mapper;
        this.queue = queue;
    }

    public AlgorithmLevelDTO execute(FullyEvaluateRequest request) {

        AlgorithmLevelDTO dto;

        if (request.previousPreTriageId != null) {
            CriticalCheckTriageAssessed prevAssessment = queue.findInProcessOrFail(request.previousPreTriageId);
            dto = execute(request, prevAssessment);
            queue.finish(prevAssessment);
        } else {
            dto = execute(request, null);
        }

        return dto;
    }

    protected AlgorithmLevelDTO execute(
            FullyEvaluateRequest request,
            CriticalCheckTriageAssessed prevAssesment
    ) {
        ChiefComplaint selectedChiefComplaint = new ChiefComplaint(
                new ClinicalFindingId(request.triageChiefComplaintId), null);

        Triage selectedTriage = repository.find(selectedChiefComplaint);

        if (null == selectedTriage) {
            throw TriageDoesNotExistException.withChiefComplaint(selectedChiefComplaint);
        }

        AlgorithmLevel output = selectedTriage.fullyAssess(
                request.findingIds.parallelStream().map(fStr -> new ClinicalFinding(
                        new ClinicalFindingId(fStr), null)).collect(Collectors.toList()),
                prevAssesment
        );
        return mapper.mapAlgorithmLevel(output);
    }
}
