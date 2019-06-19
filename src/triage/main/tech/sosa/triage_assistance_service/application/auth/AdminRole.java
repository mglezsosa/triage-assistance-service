package tech.sosa.triage_assistance_service.application.auth;

import javax.servlet.http.HttpServletRequest;

public class AdminRole implements Role {

    @Override
    public String getName() {
        return "admin";
    }

    @Override
    public boolean isAuthorized(HttpServletRequest request) {
        return true;
    }
}
