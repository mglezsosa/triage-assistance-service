package tech.sosa.triage_assistance_service.infrastructure.springframework.boot.controller;

import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.sosa.triage_assistance_service.application.TriageMapper;
import tech.sosa.triage_assistance_service.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.application.service.CreateTriage;
import tech.sosa.triage_assistance_service.application.service.CreateTriageRequest;
import tech.sosa.triage_assistance_service.application.service.DeleteTriage;
import tech.sosa.triage_assistance_service.application.service.DeleteTriageRequest;
import tech.sosa.triage_assistance_service.application.service.UpdateTriage;
import tech.sosa.triage_assistance_service.application.service.UpdateTriageRequest;
import tech.sosa.triage_assistance_service.application.service.ViewAllTriages;
import tech.sosa.triage_assistance_service.application.service.ViewTriage;
import tech.sosa.triage_assistance_service.application.service.ViewTriageRequest;
import tech.sosa.triage_assistance_service.domain.model.TriageRepository;

@RestController
public class TriageController {

    private TriageRepository repository;
    private TriageMapper mapper;

    public TriageController(
            TriageRepository repository,
            TriageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @PostMapping("/triage/")
    public ResponseEntity insertTriage(@RequestBody final CreateTriageRequest request) {
        new CreateTriage(repository, mapper).execute(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/triage/")
    public ResponseEntity updateTriage(@RequestBody final UpdateTriageRequest request) {
        new UpdateTriage(repository, mapper).execute(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/triage/{id}")
    public TriageDTO viewTriage(@PathVariable("id") final String id) {
        return new ViewTriage(repository, mapper)
                .execute(new ViewTriageRequest(id));
    }

    @GetMapping("/triage/")
    public Collection<TriageDTO> viewAllTriages() {
        return new ViewAllTriages(repository, mapper).execute();
    }

    @DeleteMapping("/triage/{id}")
    public ResponseEntity deleteTriage(@PathVariable("id") final String id) {
        new DeleteTriage(repository, mapper)
                .execute(new DeleteTriageRequest(id));
        return ResponseEntity.ok().build();
    }
}
