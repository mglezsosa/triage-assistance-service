package tech.sosa.triage_assistance_service.identity_access.domain.model;

import tech.sosa.triage_assistance_service.shared.application.service.ApplicationRequest;

import java.util.Collections;

public class AdminRole extends Role {

    public AdminRole() {
        super(Collections.emptyList());
    }

    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public boolean hasAuthorization(String useCaseName) {
        return true;
    }

}
