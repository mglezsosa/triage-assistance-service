package tech.sosa.triage_assistance_service.application.service;

import java.util.Collection;
import java.util.stream.Collectors;
import tech.sosa.triage_assistance_service.application.ApplicationService;
import tech.sosa.triage_assistance_service.application.NullRequest;
import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

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
