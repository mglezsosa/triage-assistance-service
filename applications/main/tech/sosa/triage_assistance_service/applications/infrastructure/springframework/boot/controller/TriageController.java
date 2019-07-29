package tech.sosa.triage_assistance_service.applications.infrastructure.springframework.boot.controller;

import java.util.Collection;

import org.everit.json.schema.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.sosa.triage_assistance_service.applications.application.AuthService;
import tech.sosa.triage_assistance_service.applications.application.TriageJSONSchemaValidationApplicationService;
import tech.sosa.triage_assistance_service.applications.port.adapter.JWTAuthData;
import tech.sosa.triage_assistance_service.applications.application.SecuredApplicationService;
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
    private AuthService authService;
    private Schema jsonSchema;

    public TriageController(
            TriageRepository repository,
            TriageMapper mapper,
            PendingTriagesQueue queue,
            AuthService authService,
            Schema jsonSchema) {
        this.repository = repository;
        this.mapper = mapper;
        this.queue = queue;
        this.authService = authService;
        this.jsonSchema = jsonSchema;
    }

    @PostMapping("/critical-only")
    public CriticalCheckAssesmentOutput checkForCriticalStatus(@RequestBody final CheckForCriticalStateRequest request,
                                                               @RequestBody String body,
                                                               @RequestHeader("Authorization") String authHeader) {

        return new SecuredApplicationService<>(
                new CheckForCriticalState(repository, queue),
                authService,
                JWTAuthData.fromAuthorizationHeaderString(
                        authHeader,
                        CheckForCriticalState.class.getName(),
                        body
                )
        ).execute(request);
    }

    @PostMapping("/full")
    public AlgorithmLevelDTO fullyEvaluate(@RequestBody final FullyEvaluateRequest request,
                                           @RequestBody String body,
                                           @RequestHeader("Authorization") String authHeader) {

        return new SecuredApplicationService<>(
                new FullyEvaluate(repository, mapper, queue),
                authService,
                JWTAuthData.fromAuthorizationHeaderString(
                        authHeader,
                        FullyEvaluate.class.getName(),
                        body
                )
        ).execute(request);
    }

    @PostMapping
    public ResponseEntity insertTriage(@RequestBody final CreateTriageRequest request,
                                       @RequestBody String body,
                                       @RequestHeader("Authorization") String authHeader) {

        new SecuredApplicationService<>(
                new TriageJSONSchemaValidationApplicationService<>(
                        new CreateTriage(repository, mapper),
                        jsonSchema,
                        body
                ),
                authService,
                JWTAuthData.fromAuthorizationHeaderString(
                        authHeader,
                        CreateTriage.class.getName(),
                        body
                )
        ).execute(request);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity updateTriage(@RequestBody final UpdateTriageRequest request,
                                       @RequestBody String body,
                                       @RequestHeader("Authorization") String authHeader) {

        new SecuredApplicationService<>(
                new TriageJSONSchemaValidationApplicationService<>(
                        new UpdateTriage(repository, mapper),
                        jsonSchema,
                        body
                ),
                authService,
                JWTAuthData.fromAuthorizationHeaderString(
                        authHeader,
                        UpdateTriage.class.getName(),
                        body
                )
        ).execute(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public TriageDTO viewTriage(@PathVariable("id") final String id,
                                @RequestBody String body,
                                @RequestHeader("Authorization") String authHeader) {

        return new SecuredApplicationService<>(
                new ViewTriage(repository, mapper),
                authService,
                JWTAuthData.fromAuthorizationHeaderString(
                        authHeader,
                        ViewTriage.class.getName(),
                        body
                )
        ).execute(new ViewTriageRequest(id));
    }

    @GetMapping
    public Collection<TriageDTO> viewAllTriages(
            @RequestBody String body,
            @RequestHeader("Authorization") String authHeader) {

        return new SecuredApplicationService<>(
                new ViewAllTriages(repository, mapper),
                authService,
                JWTAuthData.fromAuthorizationHeaderString(
                        authHeader,
                        ViewAllTriages.class.getName(),
                        body
                )
        ).execute(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTriage(@PathVariable("id") final String id,
                                       @RequestBody String body,
                                       @RequestHeader("Authorization") String authHeader) {

        new SecuredApplicationService<>(
                new DeleteTriage(repository, mapper),
                authService,
                JWTAuthData.fromAuthorizationHeaderString(
                        authHeader,
                        DeleteTriage.class.getName(),
                        body
                )
        ).execute(new DeleteTriageRequest(id));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/next-enqueued")
    public CriticalCheckTriageAssessed fetchNextQueued(
            @RequestBody String body,
            @RequestHeader("Authorization") String authHeader) {

        return new SecuredApplicationService<>(
                new NextEnqueuedTriage(queue),
                authService,
                JWTAuthData.fromAuthorizationHeaderString(
                        authHeader,
                        NextEnqueuedTriage.class.getName(),
                        body
                )
        ).execute(null);
    }

}
