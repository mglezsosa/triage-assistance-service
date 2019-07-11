package tech.sosa.triage_assistance_service.identity_access.domain.model;

import java.util.Collection;

public abstract class Role {

    private Collection<String> authorizedServices;

    protected Role(Collection<String> authorizedServices) {
        this.authorizedServices = authorizedServices;
    }

    public abstract String getName();

    public boolean hasAuthorization(String useCaseName) {
        return authorizedServices.contains(useCaseName);
    }

}
