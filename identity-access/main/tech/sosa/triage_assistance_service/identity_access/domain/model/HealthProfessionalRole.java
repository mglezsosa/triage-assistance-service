package tech.sosa.triage_assistance_service.identity_access.domain.model;

import java.util.Arrays;

public class HealthProfessionalRole extends Role {

    public HealthProfessionalRole() {
        super(Arrays.asList(
                "tech.sosa.triage_assistance_service.triage_evaluations.application.service.ViewAllTriages",
                "tech.sosa.triage_assistance_service.triage_evaluations.application.service.ViewTriage",
                "tech.sosa.triage_assistance_service.triage_evaluations.application.service.CheckForCriticalState",
                "tech.sosa.triage_assistance_service.triage_evaluations.application.service.FullyEvaluate",
                "tech.sosa.triage_assistance_service.triage_evaluations.application.service.NextEnqueuedTriage",
                "tech.sosa.triage_assistance_service.triage_evaluations.application.service.ViewNumberOfEnqueuedCases"
        ));
    }

    @Override
    public String getName() {
        return "health-professional";
    }

}
