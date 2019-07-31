package tech.sosa.triage_assistance_service.applications.infrastructure.springframework.boot.controller;

import org.json.JSONObject;
import org.snomed.languages.scg.SCGException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.sosa.triage_assistance_service.identity_access.domain.model.InvalidCredentialsException;
import tech.sosa.triage_assistance_service.identity_access.domain.model.NotAuthorizedException;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageAlreadyExistsException;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageDoesNotExistException;

@ControllerAdvice
public class TriageErrorAdvisor {

    @ExceptionHandler({TriageDoesNotExistException.class})
    public ResponseEntity handleNonExistingTriage(TriageDoesNotExistException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(new JSONObject().put("message", e.getMessage()).toString());
    }

    @ExceptionHandler({TriageAlreadyExistsException.class})
    public ResponseEntity handleAlreadyExistingTriage(TriageAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(new JSONObject().put("message", e.getMessage()).toString());
    }

    @ExceptionHandler({SCGException.class})
    public ResponseEntity handleIncorrectSNOMEDCTExpression(SCGException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(new JSONObject().put("message", e.getMessage()).toString());
    }

    @ExceptionHandler({NotAuthorizedException.class})
    public ResponseEntity handleNotAuthorized(NotAuthorizedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(new JSONObject().put("message", e.getMessage()).toString());
    }

    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity handleNotAuthenticated(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(new JSONObject().put("message", e.getMessage()).toString());
    }
}
