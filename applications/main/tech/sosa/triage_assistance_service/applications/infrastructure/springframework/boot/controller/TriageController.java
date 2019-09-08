package tech.sosa.triage_assistance_service.applications.infrastructure.springframework.boot.controller;

import java.util.Collection;

import com.rabbitmq.client.Channel;
import org.everit.json.schema.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.sosa.triage_assistance_service.applications.application.AuthorizationService;
import tech.sosa.triage_assistance_service.applications.application.EventPublisherResetApplicationService;
import tech.sosa.triage_assistance_service.applications.application.TriageJSONSchemaValidationApplicationService;
import tech.sosa.triage_assistance_service.applications.application.SecuredApplicationService;
import tech.sosa.triage_assistance_service.applications.port.adapter.JWTAuthorizationData;
import tech.sosa.triage_assistance_service.shared.domain.event.EventStore;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.shared.application.dto.AlgorithmLevelDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.application.service.*;
import tech.sosa.triage_assistance_service.shared.application.dto.TriageDTO;
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
    private AuthorizationService authService;
    private Schema jsonSchema;
    private EventStore eventStore;
    private Channel rabbitMQChannel;

    public TriageController(
            TriageRepository repository,
            TriageMapper mapper,
            PendingTriagesQueue queue,
            AuthorizationService authService,
            Schema jsonSchema,
            EventStore eventStore,
            Channel rabbitMQChannel) {
        this.repository = repository;
        this.mapper = mapper;
        this.queue = queue;
        this.authService = authService;
        this.jsonSchema = jsonSchema;
        this.eventStore = eventStore;
        this.rabbitMQChannel = rabbitMQChannel;
    }

    @PostMapping("/critical-only")
    public CriticalCheckAssesmentOutput checkForCriticalStatus(@RequestBody final CheckForCriticalStateRequest request,
                                                               @RequestBody String body,
                                                               @RequestHeader("Authorization") String authHeader) {

        return new EventPublisherResetApplicationService<>(
                new SecuredApplicationService<>(
                        new CheckForCriticalState(repository, queue),
                        authService,
                        JWTAuthorizationData.fromAuthorizationHeaderString(
                                authHeader,
                                CheckForCriticalState.class.getName(),
                                body
                        )
                ),
                eventStore,
                queue,
                rabbitMQChannel
        ).execute(request);
    }

    @PostMapping("/full")
    public AlgorithmLevelDTO fullyEvaluate(@RequestBody final FullyEvaluateRequest request,
                                           @RequestBody String body,
                                           @RequestHeader("Authorization") String authHeader) {

        return new EventPublisherResetApplicationService<>(
                new SecuredApplicationService<>(
                        new FullyEvaluate(repository, mapper, queue),
                        authService,
                        JWTAuthorizationData.fromAuthorizationHeaderString(
                                authHeader,
                                FullyEvaluate.class.getName(),
                                body
                        )
                ),
                eventStore,
                queue,
                rabbitMQChannel
        ).execute(request);
    }

    @PostMapping
    public ResponseEntity insertTriage(@RequestBody final CreateTriageRequest request,
                                       @RequestBody String body,
                                       @RequestHeader("Authorization") String authHeader) {

        new EventPublisherResetApplicationService<>(
                new SecuredApplicationService<>(
                        new TriageJSONSchemaValidationApplicationService<>(
                                new CreateTriage(repository, mapper),
                                jsonSchema,
                                body
                        ),
                        authService,
                        JWTAuthorizationData.fromAuthorizationHeaderString(
                                authHeader,
                                CreateTriage.class.getName(),
                                body
                        )
                ),
                eventStore,
                queue,
                rabbitMQChannel
        ).execute(request);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity updateTriage(@RequestBody final UpdateTriageRequest request,
                                       @RequestBody String body,
                                       @RequestHeader("Authorization") String authHeader) {

        new EventPublisherResetApplicationService<>(
                new SecuredApplicationService<>(
                        new TriageJSONSchemaValidationApplicationService<>(
                                new UpdateTriage(repository, mapper),
                                jsonSchema,
                                body
                        ),
                        authService,
                        JWTAuthorizationData.fromAuthorizationHeaderString(
                                authHeader,
                                UpdateTriage.class.getName(),
                                body
                        )
                ),
                eventStore,
                queue,
                rabbitMQChannel
        ).execute(request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public TriageDTO viewTriage(@PathVariable("id") final String id,
                                @RequestBody(required = false) String body,
                                @RequestHeader("Authorization") String authHeader) {

        return new EventPublisherResetApplicationService<>(
                new SecuredApplicationService<>(
                        new ViewTriage(repository, mapper),
                        authService,
                        JWTAuthorizationData.fromAuthorizationHeaderString(
                                authHeader,
                                ViewTriage.class.getName(),
                                body
                        )
                ),
                eventStore,
                queue,
                rabbitMQChannel
        ).execute(new ViewTriageRequest(id));
    }

    @GetMapping
    public Collection<TriageDTO> viewAllTriages(
            @RequestBody(required = false) String body,
            @RequestHeader("Authorization") String authHeader) {

        return new EventPublisherResetApplicationService<>(
                new SecuredApplicationService<>(
                        new ViewAllTriages(repository, mapper),
                        authService,
                        JWTAuthorizationData.fromAuthorizationHeaderString(
                                authHeader,
                                ViewAllTriages.class.getName(),
                                body
                        )
                ),
                eventStore,
                queue,
                rabbitMQChannel
        ).execute(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTriage(@PathVariable("id") final String id,
                                       @RequestBody(required = false) String body,
                                       @RequestHeader("Authorization") String authHeader) {


        new EventPublisherResetApplicationService<>(
                new SecuredApplicationService<>(
                        new DeleteTriage(repository, mapper),
                        authService,
                        JWTAuthorizationData.fromAuthorizationHeaderString(
                                authHeader,
                                DeleteTriage.class.getName(),
                                body
                        )
                ),
                eventStore,
                queue,
                rabbitMQChannel
        ).execute(new DeleteTriageRequest(id));

        return ResponseEntity.ok().build();
    }

    @GetMapping("/next-enqueued")
    public CriticalCheckTriageAssessed fetchNextQueued(
            @RequestBody(required = false) String body,
            @RequestHeader("Authorization") String authHeader) {

        return new EventPublisherResetApplicationService<>(
                new SecuredApplicationService<>(
                        new NextEnqueuedTriage(queue),
                        authService,
                        JWTAuthorizationData.fromAuthorizationHeaderString(
                                authHeader,
                                NextEnqueuedTriage.class.getName(),
                                body
                        )
                ),
                eventStore,
                queue,
                rabbitMQChannel
        ).execute(null);
    }

    @GetMapping("/queue-size")
    public Long viewNumberOfEnqueuedCases(
            @RequestBody(required = false) String body,
            @RequestHeader("Authorization") String authHeader) {

        return new EventPublisherResetApplicationService<>(
                new SecuredApplicationService<>(
                        new ViewNumberOfEqueuedCases(queue),
                        authService,
                        JWTAuthorizationData.fromAuthorizationHeaderString(
                                authHeader,
                                NextEnqueuedTriage.class.getName(),
                                body
                        )
                ),
                eventStore,
                queue,
                rabbitMQChannel
        ).execute(null);
    }

}
