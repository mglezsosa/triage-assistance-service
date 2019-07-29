package tech.sosa.triage_assistance_service.shared.port.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Logger;

import tech.sosa.triage_assistance_service.shared.domain.event.Event;
import tech.sosa.triage_assistance_service.shared.domain.event.EventStore;

public class LoggingEventStore implements EventStore {

    private Logger logger;
    private ObjectMapper objectMapper;

    public LoggingEventStore(ObjectMapper objectMapper) {
        this.logger = Logger.getLogger(LoggingEventStore.class.getName());
        this.objectMapper = objectMapper;
    }

    @Override
    public void store(Event event) {
        try {
            logger.info(objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
