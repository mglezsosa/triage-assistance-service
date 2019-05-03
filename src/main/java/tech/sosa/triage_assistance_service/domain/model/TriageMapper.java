package tech.sosa.triage_assistance_service.domain.model;

public interface TriageMapper<T> {

    Triage from(T source);

    T to(Triage aTriage);

}
