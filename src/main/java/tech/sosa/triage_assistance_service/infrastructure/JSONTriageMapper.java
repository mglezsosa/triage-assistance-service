package tech.sosa.triage_assistance_service.infrastructure;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.json.JSONArray;
import org.json.JSONObject;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaint;
import tech.sosa.triage_assistance_service.domain.model.ChiefComplaintId;
import tech.sosa.triage_assistance_service.domain.model.ClinicalFinding;
import tech.sosa.triage_assistance_service.domain.model.ClinicalFindingId;
import tech.sosa.triage_assistance_service.domain.model.EmergencyLevel;
import tech.sosa.triage_assistance_service.domain.model.Triage;
import tech.sosa.triage_assistance_service.domain.model.TriageAlgorithm;
import tech.sosa.triage_assistance_service.domain.model.TriageMapper;

public class JSONTriageMapper implements TriageMapper<JSONObject> {

    @Override
    public Triage from(JSONObject root) {

        ChiefComplaint chiefComplaint = new ChiefComplaint(
                new ChiefComplaintId(root.getString("chiefComplaint")));
        TriageAlgorithm algorithm = parseTriageAlgorithm(root.getJSONObject("algorithm"));

        return new Triage(chiefComplaint, algorithm);
    }

    private TriageAlgorithm parseTriageAlgorithm(JSONObject jsonTriageAlgorithm) {

        EmergencyLevel emLevel = new EmergencyLevel(jsonTriageAlgorithm.getString("emergencyLevel"));

        JSONArray optAdvices = jsonTriageAlgorithm.optJSONArray("advices");
        Collection<String> advices = parseAdvices(optAdvices);

        JSONArray optDiscriminators = jsonTriageAlgorithm.optJSONArray("discriminators");
        Collection<ClinicalFinding> discriminators = parseClinicalFindings(optDiscriminators);

        JSONObject optLowerLevelAlg = jsonTriageAlgorithm.optJSONObject("default");
        TriageAlgorithm lowerLevelAlg = parseLowerLevelAlgorithm(optLowerLevelAlg);

        return new TriageAlgorithm(emLevel, discriminators, advices, lowerLevelAlg);
    }

    private TriageAlgorithm parseLowerLevelAlgorithm(JSONObject optLowerLevelAlg) {
        TriageAlgorithm lowerLevelAlg = null;
        if (optLowerLevelAlg != null) {
            lowerLevelAlg = parseTriageAlgorithm(optLowerLevelAlg);
        }
        return lowerLevelAlg;
    }

    private Collection<String> parseAdvices(JSONArray optAdvices) {
        Collection<String> advices = null;
        if (optAdvices != null) {
            advices = StreamSupport.stream(optAdvices.spliterator(), true)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        return advices;
    }

    private Collection<ClinicalFinding> parseClinicalFindings(JSONArray optDiscriminators) {
        Collection<ClinicalFinding> discriminators = null;
        if (optDiscriminators != null) {
            discriminators = StreamSupport.stream(optDiscriminators.spliterator(), true)
                    .map(sctExpression -> new ClinicalFinding(
                            new ClinicalFindingId(sctExpression.toString())))
                    .collect(Collectors.toList());
        }
        return discriminators;
    }

    @Override
    public JSONObject to(Triage aTriage) {
        return null;
    }
}
