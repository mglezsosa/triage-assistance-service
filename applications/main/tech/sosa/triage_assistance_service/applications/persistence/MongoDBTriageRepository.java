package tech.sosa.triage_assistance_service.applications.persistence;

import static com.mongodb.client.model.Filters.eq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import org.bson.Document;
import tech.sosa.triage_assistance_service.triage_evaluations.application.TriageMapper;
import tech.sosa.triage_assistance_service.shared.application.dto.TriageDTO;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.Triage;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.TriageRepository;


public class MongoDBTriageRepository implements TriageRepository {

    private MongoCollection<Document> collection;
    private ObjectMapper jsonMapper;
    private TriageMapper dtoMapper;

    public MongoDBTriageRepository(
            MongoCollection<Document> collection,
            TriageMapper dtoMapper,
            ObjectMapper jsonMapper) {
        this.collection = collection;
        this.jsonMapper = jsonMapper;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public Triage find(ChiefComplaint complaint) {
        Document bsonDocument = collection
                .find(eq("chiefComplaint.id", complaint.id().value()))
                .projection(new Document("_id", 0))
                .first();

        if (null == bsonDocument) {
            return null;
        }

        try {
            return from(bsonDocument);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Triage from(Document bsonDocument) throws IOException {
        return dtoMapper.from(jsonMapper.readValue(bsonDocument.toJson(), TriageDTO.class));
    }

    @Override
    public void save(Triage triage) {
        try {
            collection.replaceOne(
                    eq("chiefComplaint.id", triage.chiefComplaint().id().value()),
                    Document.parse(jsonMapper.writeValueAsString(dtoMapper.to(triage))),
                    new ReplaceOptions().upsert(true).bypassDocumentValidation(false)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Triage triage) {
        collection.deleteOne(eq("chiefComplaint.id", triage.chiefComplaint().id().value()));
    }

    @Override
    public Collection<Triage> all() {
        Collection<Triage> triages = new ArrayList<>();
        collection.find()
                .projection(new Document("_id", 0))
                .map(d -> {
                    try {
                        return from(d);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).iterator().forEachRemaining(triages::add);
        return triages;
    }
}
