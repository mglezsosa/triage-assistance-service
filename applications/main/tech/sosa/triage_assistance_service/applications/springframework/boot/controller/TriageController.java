package tech.sosa.triage_assistance_service.applications.springframework.boot.controller;

import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.sosa.triage_assistance_service.identity_access.application.service.Authorize;
import tech.sosa.triage_assistance_service.identity_access.application.service.AuthorizeRequest;
import tech.sosa.triage_assistance_service.identity_access.port.adapter.HardcodedAuthService;
import tech.sosa.triage_assistance_service.shared.application.service.SecuredApplicationService;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.shared.application.dto.AlgorithmLevelDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.CheckForCriticalStateRequest;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.FullyEvaluateRequest;
import tech.sosa.triage_assistance_service.shared.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.CheckForCriticalState;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.CreateTriage;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.CreateTriageRequest;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.DeleteTriage;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.DeleteTriageRequest;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.FullyEvaluate;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.NextEnqueuedTriage;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.UpdateTriage;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.UpdateTriageRequest;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.ViewAllTriages;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.ViewTriage;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.ViewTriageRequest;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.CriticalCheckAssesmentOutput;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;

@RestController
@RequestMapping("/triage")
public class TriageController {

    private TriageRepository repository;
    private TriageMapper mapper;
    private PendingTriagesQueue queue;
    private Authorize authorizeUseCase;

    public TriageController(
            TriageRepository repository,
            TriageMapper mapper,
            PendingTriagesQueue queue,
            Authorize authorize) {
        this.repository = repository;
        this.mapper = mapper;
        this.queue = queue;
        this.authorizeUseCase = authorize;
    }

    @PostMapping("/critical-only")
    public CriticalCheckAssesmentOutput checkForCriticalStatus(@RequestBody final CheckForCriticalStateRequest request,
                                                               @RequestBody String body,
                                                               @RequestHeader("Authorization") String authHeader) {
        assert authHeader.startsWith("Bearer ");
        String authInfo = authHeader.substring(7);

        return new SecuredApplicationService<>(
                new CheckForCriticalState(repository),
                authorizeUseCase,
                new AuthorizeRequest(
                        authInfo,
                        CheckForCriticalState.class.getName(),
                        body
                )
        ).execute(request);
    }

    @PostMapping("/full")
    public AlgorithmLevelDTO fullyEvaluate(@RequestBody final FullyEvaluateRequest request) {
        return new FullyEvaluate(repository, mapper, queue).execute(request);
    }

    @PostMapping
    public ResponseEntity insertTriage(@RequestBody final CreateTriageRequest request) {
        new CreateTriage(repository, mapper).execute(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity updateTriage(@RequestBody final UpdateTriageRequest request) {
        new UpdateTriage(repository, mapper).execute(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public TriageDTO viewTriage(@PathVariable("id") final String id) {
        return new ViewTriage(repository, mapper).execute(new ViewTriageRequest(id));
    }

    @GetMapping
    public Collection<TriageDTO> viewAllTriages() {
        return new ViewAllTriages(repository, mapper).execute(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTriage(@PathVariable("id") final String id) {
        new DeleteTriage(repository, mapper)
                .execute(new DeleteTriageRequest(id));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/next-enqueued")
    public CriticalCheckTriageAssessed fetchNextQueued() {
        return new NextEnqueuedTriage(queue).execute(null);
    }

}
