package tech.sosa.triage_assistance_service.infrastructure.springframework.boot.controller;

import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.application.dto.AlgorithmLevelDTO;
import tech.sosa.triage_assistance_service.application.service.CheckForCriticalStateRequest;
import tech.sosa.triage_assistance_service.application.service.FullyEvaluateRequest;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.application.service.CheckForCriticalState;
import tech.sosa.triage_assistance_service.application.service.CreateTriage;
import tech.sosa.triage_assistance_service.application.service.CreateTriageRequest;
import tech.sosa.triage_assistance_service.application.service.DeleteTriage;
import tech.sosa.triage_assistance_service.application.service.DeleteTriageRequest;
import tech.sosa.triage_assistance_service.application.service.FullyEvaluate;
import tech.sosa.triage_assistance_service.application.service.NextEnqueuedTriage;
import tech.sosa.triage_assistance_service.application.service.UpdateTriage;
import tech.sosa.triage_assistance_service.application.service.UpdateTriageRequest;
import tech.sosa.triage_assistance_service.application.service.ViewAllTriages;
import tech.sosa.triage_assistance_service.application.service.ViewTriage;
import tech.sosa.triage_assistance_service.application.service.ViewTriageRequest;
import tech.sosa.triage_assistance_service.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.domain.model.CriticalCheckAssesmentOutput;
import tech.sosa.triage_assistance_service.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

@RestController
@RequestMapping("/triage")
public class TriageController {

    private TriageRepository repository;
    private TriageMapper mapper;
    private PendingTriagesQueue queue;

    public TriageController(
            TriageRepository repository,
            TriageMapper mapper,
            PendingTriagesQueue queue) {
        this.repository = repository;
        this.mapper = mapper;
        this.queue = queue;
    }

    @PostMapping("/critical-only")
    public CriticalCheckAssesmentOutput checkForCriticalStatus(@RequestBody final CheckForCriticalStateRequest request) {
        return new CheckForCriticalState(repository).execute(request);
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
