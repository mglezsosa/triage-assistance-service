package tech.sosa.triage_assistance_service.shared.application.service;

public interface ApplicationService<T, S extends ApplicationRequest> {

    T execute(S request);

}
