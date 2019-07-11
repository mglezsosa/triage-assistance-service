package tech.sosa.triage_assistance_service.identity_access.domain.model;

import java.util.Arrays;

public class TelephoneOperatorRole extends Role {

    public TelephoneOperatorRole() {
        super(Arrays.asList(
                "tech.sosa.triage_assistance_service.triage_evaluations.application.service.ViewAllTriages",
                "tech.sosa.triage_assistance_service.triage_evaluations.application.service.ViewTriage",
                "tech.sosa.triage_assistance_service.triage_evaluations.application.service.CheckForCriticalState"
        ));
    }

    @Override
    public String getName() {
        return "telephone-operator";
    }

}
