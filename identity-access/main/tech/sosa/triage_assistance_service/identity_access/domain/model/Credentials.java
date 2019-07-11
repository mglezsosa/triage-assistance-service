package tech.sosa.triage_assistance_service.identity_access.domain.model;

public class Credentials {

    private String codedInfo;

    public Credentials(String codedInfo) {
        this.codedInfo = codedInfo;
    }

    public String codedInfo() {
        return codedInfo;
    }

    public Credentials changeInfo(String newInfo) {
        return new Credentials(newInfo);
    }

}
