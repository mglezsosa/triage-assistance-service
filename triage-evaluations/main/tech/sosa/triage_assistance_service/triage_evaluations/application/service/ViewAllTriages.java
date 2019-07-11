package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import java.util.Collection;
import java.util.stream.Collectors;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;
import tech.sosa.triage_assistance_service.triage_evaluations.application.NullRequest;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.shared.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;

public class ViewAllTriages implements ApplicationService<Collection<TriageDTO>, NullRequest> {

    private final TriageMapper triageMapper;
    private final TriageRepository triageRepo;

    public ViewAllTriages(TriageRepository triageRepo, TriageMapper triageMapper) {
        this.triageRepo = triageRepo;
        this.triageMapper = triageMapper;
    }

    public Collection<TriageDTO> execute(NullRequest request) {
        return triageRepo.all().stream().map(triageMapper::to).collect(Collectors.toList());
    }
}
