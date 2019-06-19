package tech.sosa.triage_assistance_service.application;

public interface ApplicationService<T, S extends ApplicationRequest> {

    T execute(S request);

}
