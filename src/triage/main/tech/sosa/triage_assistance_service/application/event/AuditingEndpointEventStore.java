package tech.sosa.triage_assistance_service.application.event;

import tech.sosa.triage_assistance_service.util.event.EventStore;

public interface AuditingEndpointEventStore extends EventStore<AuthenticatedEndpointReached> {

}
