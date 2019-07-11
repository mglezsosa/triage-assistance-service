package tech.sosa.triage_assistance_service.triage_evaluations.application.service;

import java.util.stream.Collectors;
import tech.sosa.triage_assistance_service.shared.application.service.ApplicationService;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFinding;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.CriticalCheckAssesmentOutput;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.Triage;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageDoesNotExistException;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;

public class CheckForCriticalState
        implements ApplicationService<CriticalCheckAssesmentOutput, CheckForCriticalStateRequest> {

    private final TriageRepository repository;

    public CheckForCriticalState(TriageRepository repository) {
        this.repository = repository;
    }

    public CriticalCheckAssesmentOutput execute(CheckForCriticalStateRequest request) {
        ChiefComplaint selectedChiefComplaint = new ChiefComplaint(
                new ClinicalFindingId(request.triageChiefComplaintId), null);

        Triage selectedTriage = repository.find(selectedChiefComplaint);

        if (null == selectedTriage) {
            throw TriageDoesNotExistException.withChiefComplaint(selectedChiefComplaint);
        }

        return selectedTriage.checkForCriticalState(
                request.findingIds.parallelStream().map(fStr -> new ClinicalFinding(
                        new ClinicalFindingId(fStr), null)).collect(Collectors.toList())
        );
    }

}
