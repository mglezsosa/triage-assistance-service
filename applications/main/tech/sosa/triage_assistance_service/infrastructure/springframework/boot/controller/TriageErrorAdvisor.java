package tech.sosa.triage_assistance_service.infrastructure.springframework.boot.controller;

import org.json.JSONObject;
import org.snomed.languages.scg.SCGException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.sosa.triage_assistance_service.domain.model.TriageAlreadyExistsException;
import tech.sosa.triage_assistance_service.domain.model.TriageDoesNotExistException;

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
}
