package tech.sosa.triage_assistance_service.applications.port.adapter;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import java.io.IOException;
import java.util.NoSuchElementException;
import org.bson.Document;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.event.CriticalCheckTriageAssessed;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.ClinicalFindingTitle;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.CriticalCheckAssesmentOutput;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.PendingTriagesQueue;
import tech.sosa.triage_assistance_service.triage_evaluations.domain.model.StatefulCriticalCheckTriageAssessed;

public class MongoDBPendingTriagesQueue implements PendingTriagesQueue {

    private ObjectMapper objectMapper;
    private MongoCollection<Document> collection;

    public MongoDBPendingTriagesQueue(ObjectMapper objectMapper,
            MongoCollection<Document> collection) {
        this.objectMapper = objectMapper;
        this.collection = collection;
    }

    @Override
    public void enqueue(CriticalCheckTriageAssessed assessing) {
        try {
            collection.insertOne(Document.parse(objectMapper.writeValueAsString(
                    StatefulCriticalCheckTriageAssessed.create(assessing)
            )));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CriticalCheckTriageAssessed findInProcessOrFail(String id) {
        Document bsonDocument = collection
                .find(and(eq("id", id), eq("status", StatefulCriticalCheckTriageAssessed.Status.IN_PROCESS.toString())))
                .projection(new Document("_id", 0))
                .first();

        if (null == bsonDocument) {
            throw new NoSuchElementException("No value present");
        }

        try {
            return from(bsonDocument);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private StatefulCriticalCheckTriageAssessed from(Document bsonDocument) throws IOException {
        Document outputDoc = bsonDocument.get("output", Document.class);
        return new StatefulCriticalCheckTriageAssessed(
                bsonDocument.getString("id"),
                new ChiefComplaint(
                        new ClinicalFindingId(bsonDocument.getString("foundChiefComplaintId")),
                        new ClinicalFindingTitle(bsonDocument.getString("foundChiefComplaintTitle"))
                ),
                new CriticalCheckAssesmentOutput(
                        outputDoc.getBoolean("hasCriticalState"),
                        outputDoc.getList("advices", String.class)
                ),
                StatefulCriticalCheckTriageAssessed.Status.valueOf(bsonDocument.getString("status"))
        );
    }

    @Override
    public CriticalCheckTriageAssessed nextPending() {
        Document bsonDocument = collection
                .findOneAndUpdate(
                        eq("status", StatefulCriticalCheckTriageAssessed.Status.PENDING.toString()),
                        new Document().append("$set",
                                new Document().append("status", StatefulCriticalCheckTriageAssessed.Status.IN_PROCESS.toString())),
                        new FindOneAndUpdateOptions().projection(new Document().append("_id", 0)));

        if (null == bsonDocument) {
            throw new NoSuchElementException("No value present");
        }

        try {
            return from(bsonDocument);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void finish(CriticalCheckTriageAssessed assessing) {
        Document deletedAssessing = collection.findOneAndDelete(
                and(
                        eq("id", assessing.getId()),
                        eq("status", StatefulCriticalCheckTriageAssessed.Status.IN_PROCESS.toString())
                )
        );

        if (null == deletedAssessing) {
            throw new NoSuchElementException("No value present");
        }
    }

    @Override
    public long numberOfEnqueuedCases() {
        return collection.countDocuments(
                eq("status", StatefulCriticalCheckTriageAssessed.Status.PENDING.toString())
        );
    }
}
